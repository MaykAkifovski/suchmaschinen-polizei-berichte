package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedLoader;
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
    private PoliceReportTransformedLoader policeReportTransformedLoader;

    @Autowired
    private TextTokenizer textService;

    @Autowired
    private FullTextSearch fullTextSearch;

    public List<RankedPoliceReport> getPoliceReportsSortedByScore(FrontEndRequest frontEndRequest) {
        List<String> searchStrings = textService.transform(frontEndRequest.getSearchString());
        List<PoliceReportTransformed> allReports = policeReportTransformedLoader.getPoliceReportTransformedList();
        Stream<PoliceReportTransformed> filteredReports = filterReports(allReports, frontEndRequest);

        return filteredReports
                .map(report -> fullTextSearch.run(report, searchStrings))
                .filter(o -> o.getScore() != 0)
                .sorted(Comparator.comparing(RankedPoliceReport::getScore, Integer::compareTo).reversed())
                .collect(Collectors.toList());
    }

    private Stream<PoliceReportTransformed> filterReports(List<PoliceReportTransformed> allReports, FrontEndRequest frontEndRequest) {
        try {
            long startDate = parseDate(frontEndRequest.getSearchDateRange().get(0));
            long endDate = parseDate(frontEndRequest.getSearchDateRange().get(1));

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

        if (locationsInRequest != null && locationsInRequest.get(0).length() > 0){
            return allReports
                    .stream()
                    .filter(policeReportTransformed -> locationsInRequest.contains(policeReportTransformed.getLocation()));
        }
        return allReports.stream();
    }

    private boolean filterByDate(long date, long startDate, long endDate) {
        return startDate <= date && endDate >= date;
    }
}
