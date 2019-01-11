package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PoliceReportsRanker {

    @Autowired
    private PoliceReportTransformedRepository repository;

    @Autowired
    private TextTransformerService textService;

    @Autowired
    private FullTextSearchService fullTextSearchService;

    public List<RankedPoliceReport> getTop10PoliceReports(FrontEndRequest frontEndRequest) {
        List<String> searchStrings = textService.transform(frontEndRequest.getSearchString());
        List<PoliceReportTransformed> allReports = getAllReports();
        Stream<PoliceReportTransformed> filteredReports = filterReports(allReports, frontEndRequest);

        return filteredReports
                .map(report -> fullTextSearchService.run(report, searchStrings))
                .sorted(Comparator.comparing(RankedPoliceReport::getScore, Integer::compareTo).reversed())
                .limit(10)
                .collect(Collectors.toList());
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
