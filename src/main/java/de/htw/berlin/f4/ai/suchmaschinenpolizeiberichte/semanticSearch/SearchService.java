package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.exception.NotFoundException;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RequestObjectLog;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.ComputeSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetDetailedSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.RequestObjectLogLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        return buildResponse(rankedPoliceReports);
    }

    private List<RankedPoliceReport> getFilteredRankedPoliceReport(String searchId, int page, int pagesize) throws NotFoundException {
        return requestObjectLogLoader
                .findById(searchId)
                .orElseThrow(NotFoundException::new)
                .getResults()
                .stream()
                .skip(page * pagesize)
                .limit(pagesize)
                .collect(Collectors.toList());
    }

    private List<GetSearchResponse> buildResponse(List<RankedPoliceReport> rankedPoliceReports) {
        return rankedPoliceReports
                .stream()
                .map(this::buildOneResponse)
                .collect(Collectors.toList());
    }

    private GetSearchResponse buildOneResponse(RankedPoliceReport rankedPoliceReport) {
        PoliceReport policeReport = policeReportLoader
                .findById(rankedPoliceReport.getIdToOrigin())
                .orElse(new PoliceReport());

        PoliceReportTransformed policeReportTransformed = policeReportTransformedLoader
                .findOneByIdToOrigin(rankedPoliceReport.getIdToOrigin())
                .orElse(new PoliceReportTransformed());

        return GetSearchResponse.builder()
                .snippet(policeReport.getContent().substring(0, 100))
                .date(policeReportTransformed.getDate())
                .id(policeReport.get_id())
                .location(policeReportTransformed.getLocation())
                .title(policeReport.getTitle())
                .url(policeReport.getUrl())
                .build();
    }

    public GetDetailedSearchResponse getPoliceReportById(String searchId) throws NotFoundException {
        PoliceReport policeReport = policeReportLoader.findById(searchId).orElseThrow(NotFoundException::new);
        return GetDetailedSearchResponse.builder()
                .content(policeReport.getContent())
                .createdAt(policeReport.getCreatedAt())
                .header(policeReport.getHeader())
                .title(policeReport.getTitle())
                .url(policeReport.getUrl()).build();
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
