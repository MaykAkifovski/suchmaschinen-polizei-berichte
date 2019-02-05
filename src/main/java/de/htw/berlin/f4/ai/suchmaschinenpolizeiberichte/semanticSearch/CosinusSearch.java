package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RankedPoliceReport;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CosinusSearch {

    public RankedPoliceReport run(PoliceReportTransformed report, Map<String, Double> searchVector) {
        Double cosinusSimilarity = calculateCosinusSimilarity(searchVector, report.getTf_idf());
        return new RankedPoliceReport(report.getIdToOrigin(), cosinusSimilarity);
    }

    private Double calculateCosinusSimilarity(Map<String, Double> searchVector, Map<String, Double> reportTfIdf) {
        double dot_product = searchVector
                .entrySet()
                .stream()
                .mapToDouble(x -> x.getValue() * reportTfIdf.getOrDefault(x.getKey(), 0.))
                .sum();

        double searchVector_skalar = searchVector
                .values()
                .stream()
                .mapToDouble(value -> value * value)
                .sum();

        double reportVector_skalar = reportTfIdf
                .values()
                .stream()
                .mapToDouble(value -> value * value)
                .sum();

        return dot_product / Math.sqrt(searchVector_skalar * reportVector_skalar);
    }
}
