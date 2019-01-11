package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.*;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedRepository;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.RequestObjectLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SearchService {

    @Autowired
    private RequestObjectLogRepository requestObjectLogRepository;

    @Autowired
    private PoliceReportTransformedRepository repository;

    @Autowired
    private TextTransformerService textService;

    @Autowired
    private FullTextSearchService fullTextSearchService;


    public List<RankedPoliceReport> getSearch(String searchId, int page, int pagesize) {
        RequestObjectLog requestObjectLog = requestObjectLogRepository.findById(searchId).get();
        return requestObjectLog.getResults().subList((page - 1) * pagesize, (page - 1) * pagesize + pagesize);
    }

    public ComputeSearchResponse computeSearch(FrontEndRequest frontEndRequest) {
        List<String> searchStrings = textService.transform(frontEndRequest.getSearchString());
        List<PoliceReportTransformed> allReports = getAllReports();
        Stream<PoliceReportTransformed> filteredReports = filterReports(allReports, frontEndRequest);

        RequestObjectLog requestObjectLog = new RequestObjectLog();

        requestObjectLog.setDate(new Date().toString());
        requestObjectLog.setSearchedDatum(frontEndRequest.getSearchDaterange());
        requestObjectLog.setSearchedLocation(frontEndRequest.getSearchLocations());
        requestObjectLog.setSearchedText(frontEndRequest.getSearchString());

        List<RankedPoliceReport> results = filteredReports
                .map(report -> fullTextSearchService.run(report, searchStrings))
                .sorted(Comparator.comparing(RankedPoliceReport::getScore, Integer::compareTo).reversed())
                .collect(Collectors.toList());

        requestObjectLog.setResults(results);

        requestObjectLogRepository.save(requestObjectLog);
        return new ComputeSearchResponse(requestObjectLog.getId(), results.size());
    }

    private List<PoliceReportTransformed> getAllReports() {
        return repository.findAll();
    }

    private Stream<PoliceReportTransformed> filterReports(List<PoliceReportTransformed> allReports, FrontEndRequest frontEndRequest) {
        try {
            long startDate = parseDate(frontEndRequest.getSearchDaterange().get(0));
            long endDate = parseDate(frontEndRequest.getSearchDaterange().get(1));

            return filterByLocation(allReports, frontEndRequest.getSearchLocations())
                    .filter(policeReportTransformed -> filterByDate(policeReportTransformed.getDate(), startDate, endDate));
        } catch (ParseException ignored) {
            return filterByLocation(allReports, frontEndRequest.getSearchLocations());
        }
    }

    private long parseDate(String date) throws ParseException {
        String[] ts = date.split("T");
        return new SimpleDateFormat("yyyy-MM-dd").parse(ts[0]).getTime();
    }

    private Stream<PoliceReportTransformed> filterByLocation(List<PoliceReportTransformed> allReports, List<String> locationsInRequest) {
        return allReports
                .stream()
                .filter(policeReportTransformed -> locationsInRequest.contains(policeReportTransformed.getLocation()));
    }

    private boolean filterByDate(long date, long startDate, long endDate) {
        return startDate <= date && endDate >= date;
    }
}
