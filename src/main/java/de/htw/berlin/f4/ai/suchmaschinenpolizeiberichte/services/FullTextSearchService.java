package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.RankedPoliceReport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
public class FullTextSearchService {

    public RankedPoliceReport run(PoliceReportTransformed report, List<String> searchStrings) {
        Supplier<Stream<String>> corpus = () -> Stream.concat(report.getContent().stream(), report.getTitle().stream());
        int score = (int) searchStrings.stream().filter(str -> corpus.get().anyMatch(s -> s.equals(str))).count();
        return new RankedPoliceReport(report.getIdToOrigin(), score);
    }
}