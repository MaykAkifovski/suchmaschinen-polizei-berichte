package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.exception.NotFoundException;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RequestObjectLog;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.ComputeSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.RequestObjectLogLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class SearchService {

    @Autowired
    private RequestObjectLogLoader requestObjectLogLoader;
    @Autowired
    private PoliceReportLoader policeReportLoader;
    @Autowired
    private PoliceReportTransformedLoader policeReportTransformedLoader;
    @Autowired
    private PoliceReportsRanker policeReportsRanker;

    public List<GetSearchResponse> getSearch(String searchId, int page, int pagesize) throws NotFoundException {
        List<RankedPoliceReport> rankedPoliceReports = getFilteredRankedPoliceReport(searchId, page, pagesize);
        return buildSearchResponse(rankedPoliceReports);
    }

    public PoliceReport getPoliceReportById(String searchId) throws NotFoundException {
        return policeReportLoader.findById(searchId).orElseThrow(NotFoundException::new);
    }

    private List<RankedPoliceReport> getFilteredRankedPoliceReport(String searchId, int page, int pagesize) throws NotFoundException {
        List<RankedPoliceReport> result = requestObjectLogLoader
                .findById(searchId)
                .orElseThrow(NotFoundException::new)
                .getResults();

        return result.stream().skip(page * pagesize).limit(pagesize).collect(Collectors.toCollection(ArrayList::new));
    }

    private List<GetSearchResponse> buildSearchResponse(List<RankedPoliceReport> rankedPoliceReports) throws NotFoundException {

        List<GetSearchResponse> results = new ArrayList<>();

        for (RankedPoliceReport rankedPoliceReportBar : rankedPoliceReports) {

            PoliceReport policeReport = policeReportLoader
                    .findById(rankedPoliceReportBar.getIdToOrigin())
                    .orElse(null);

            PoliceReportTransformed policeReportTransformed = policeReportTransformedLoader
                    .findOneByIdToOrigin(rankedPoliceReportBar.getIdToOrigin())
                    .orElseThrow(NotFoundException::new);

            GetSearchResponse searchResponse = GetSearchResponse.builder()
                    .snippet(policeReport.getContent().substring(0, 100))
                    .date(policeReportTransformed.getDate())
                    .id(policeReport.get_id())
                    .location(policeReportTransformed.getLocation())
                    .title(policeReport.getTitle())
                    .url(policeReport.getUrl())
                    .build();
            results.add(searchResponse);
        }
        return results;
    }

    public ComputeSearchResponse computeSearch(FrontEndRequest frontEndRequest) {
        List<RankedPoliceReport> rankedPoliceReports = policeReportsRanker.getPoliceReportsSortedByScore(frontEndRequest);
        RequestObjectLog requestObjectLog = createRequestObjectLog(frontEndRequest, rankedPoliceReports);
        requestObjectLogLoader.add(requestObjectLog);
        return createSearchResponse(requestObjectLog.getId(), rankedPoliceReports.size());
    }

    private RequestObjectLog createRequestObjectLog(FrontEndRequest frontEndRequest, List<RankedPoliceReport> rankedPoliceReports) {
        return RequestObjectLog.builder()
                .id(generateId())
                .date(LocalDate.now().toString())
                .searchedDatum(frontEndRequest.getSearchDateRange())
                .searchedLocation(frontEndRequest.getSearchLocations())
                .searchedText(frontEndRequest.getSearchString())
                .results(rankedPoliceReports)
                .build();
    }

    private String generateId() {
        return String.valueOf(Math.abs(new Random().nextLong()));
    }

    private ComputeSearchResponse createSearchResponse(String id, int count) {
        return ComputeSearchResponse.builder()
                .searchId(id)
                .count(count)
                .build();
    }
}
