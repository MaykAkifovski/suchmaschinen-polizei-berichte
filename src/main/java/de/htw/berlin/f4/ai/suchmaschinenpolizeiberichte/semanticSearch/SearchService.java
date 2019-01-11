package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.ComputeSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.RequestObjectLog;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.RequestObjectLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SearchService {

    @Autowired
    private RequestObjectLogRepository requestObjectLogRepository;
    @Autowired
    private PoliceReportsRanker policeReportsRanker;

    public List<RankedPoliceReport> getSearch(String searchId, int page, int pagesize) throws NotFoundException {
        return requestObjectLogRepository
                .findById(searchId)
                .orElseThrow(NotFoundException::new)
                .getResults()
                .subList((page - 1) * pagesize, (page - 1) * pagesize + pagesize);
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
                .searchedDatum(frontEndRequest.getSearchDaterange())
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
