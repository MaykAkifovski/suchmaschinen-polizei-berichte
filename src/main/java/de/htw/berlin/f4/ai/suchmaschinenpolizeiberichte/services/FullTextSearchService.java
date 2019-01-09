package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.RankedPoliceReport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class FullTextSearchService {

    public RankedPoliceReport run(PoliceReportTransformed report, List<String> searchStrings) {
      Stream<String> corpus = Stream.concat(report.getContent().stream(), report.getTitle().stream()).distinct();
        int score = (int) searchStrings.stream().filter(str -> corpus.anyMatch(s -> s.equals(str))).count();
        return new RankedPoliceReport(report.getIdToOrigin(), score);
    }
}