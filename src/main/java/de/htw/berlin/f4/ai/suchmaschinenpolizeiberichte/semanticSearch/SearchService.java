package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RequestObjectLog;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.ComputeSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedRepository;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.RequestObjectLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SearchService {

    @Autowired
    private RequestObjectLogRepository requestObjectLogRepository;

    @Autowired
    private PoliceReportLoader policeReportLoader;

    @Autowired
    private PoliceReportTransformedRepository policeReportTransformedRepository;

    @Autowired
    private PoliceReportsRanker policeReportsRanker;


    public List<GetSearchResponse> getSearch(String searchId, int page, int pagesize) throws NotFoundException {
        List<RankedPoliceReport> rankedPoliceReports = getFilteredRankedPoliceReport(searchId, page, pagesize);

        return buildSearchResponse(rankedPoliceReports);
    }

    public PoliceReport getPoliceReportById(String searchId) throws NotFoundException {
        return policeReportLoader.findById(searchId).orElseThrow(NotFoundException::new);
    }

    public List<RankedPoliceReport> getFilteredRankedPoliceReport(String searchId, int page, int pagesize) throws NotFoundException {
        return requestObjectLogRepository
                .findById(searchId)
                .orElseThrow(NotFoundException::new)
                .getResults()
                .subList((page - 1) * pagesize, (page - 1) * pagesize + pagesize);
    }

    public List<GetSearchResponse> buildSearchResponse(List<RankedPoliceReport> rankedPoliceReports) throws NotFoundException {

        List<GetSearchResponse> results = new ArrayList<>();

        for (RankedPoliceReport rankedPoliceReportBar : rankedPoliceReports) {

            PoliceReport policeReport = policeReportLoader
                    .findById(rankedPoliceReportBar.getIdToOrigin())
                    .orElse(null);

            PoliceReportTransformed policeReportTransformed = policeReportTransformedRepository
                    .findOneByIdToOrigin(rankedPoliceReportBar.getIdToOrigin());

            GetSearchResponse searchResponse = GetSearchResponse.builder()
                    .content(policeReport.getContent().substring(0, 100))
                    .date(policeReportTransformed.getDate())
                    .id(policeReport.get_id())
                    .location(policeReportTransformed.getLocation())
                    .build();
            results.add(searchResponse);
        }
        return results;
    }

    public ComputeSearchResponse computeSearch(FrontEndRequest frontEndRequest) {
        List<RankedPoliceReport> rankedPoliceReports = policeReportsRanker.getPoliceReportsSortedByScore(frontEndRequest);
        RequestObjectLog requestObjectLog = createRequestObjectLog(frontEndRequest, rankedPoliceReports);
        requestObjectLogRepository.save(requestObjectLog);
        return createSearchResponse(requestObjectLog.getId(), rankedPoliceReports.size());
    }

    private RequestObjectLog createRequestObjectLog(FrontEndRequest frontEndRequest, List<RankedPoliceReport> rankedPoliceReports) {
        return RequestObjectLog.builder()
                .date(LocalDate.now().toString())
                .searchedDatum(frontEndRequest.getSearchDateRange())
                .searchedLocation(frontEndRequest.getSearchLocations())
                .searchedText(frontEndRequest.getSearchString())
                .results(rankedPoliceReports)
                .build();
    }

    private ComputeSearchResponse createSearchResponse(String id, int count) {
        return ComputeSearchResponse.builder()
                .searchId(id)
                .count(count)
                .build();
    }
}
