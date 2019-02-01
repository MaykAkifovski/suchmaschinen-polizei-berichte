package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.TfIdfPoliceReport;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PoliceReportTransformedLoader {
    private List<PoliceReportTransformed> policeReportTransformedList = new ArrayList<>();

    private Supplier<Stream<String>> wholeCorpus;
    private Supplier<Stream<String>> uniqueCorpus;
    private Map<String, Double> idf;
    private Supplier<Stream<TfIdfPoliceReport>> tfIdfPoliceReportsList;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper om = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/policeReportsTransformed.json");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                policeReportTransformedList.add(om.readValue(line, PoliceReportTransformed.class));
            }
        }

        wholeCorpus = () -> extractWholeCorpus(policeReportTransformedList);
        uniqueCorpus = () -> wholeCorpus.get().distinct();
        idf = computeIdf(policeReportTransformedList.size());
        tfIdfPoliceReportsList = () -> computeTfIdfPoliceReports(policeReportTransformedList);
    }

    private Stream<String> extractWholeCorpus(List<PoliceReportTransformed> policeReportTransformedList) {
        return policeReportTransformedList
                .stream()
                .flatMap(this::extractCorpus);
    }

    private Stream<String> extractCorpus(PoliceReportTransformed policeReportTransformed) {
        return Stream.concat(Stream.concat(policeReportTransformed.getContent().stream(),
                policeReportTransformed.getTitle().stream()),
                policeReportTransformed.getSynonymsForContent().stream());
    }

    private Map<String, Double> computeIdf(int size) {
        return wholeCorpus
                .get()
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (1. * size) / e.getValue()));
    }

    private Stream<TfIdfPoliceReport> computeTfIdfPoliceReports(List<PoliceReportTransformed> policeReportTransformedList) {
        return policeReportTransformedList
                .stream()
                .map(this::createTfIdfPoliceReport);
    }

    private TfIdfPoliceReport createTfIdfPoliceReport(PoliceReportTransformed policeReportTransformed) {
        return TfIdfPoliceReport
                .builder()
                .idToPoliceReportTransformed(policeReportTransformed.get_id())
                .tf_idf(createTfIdf(policeReportTransformed))
                .build();
    }

    private Map<String, Double> createTfIdf(PoliceReportTransformed policeReport) {
        return extractCorpus(policeReport)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() * idf.get(e.getKey())));
    }

    public List<PoliceReportTransformed> getPoliceReportTransformedList() {
        return policeReportTransformedList;
    }

    public Optional<PoliceReportTransformed> findOneByIdToOrigin(String idToOrigin) {
        return policeReportTransformedList
                .stream()
                .filter(policeReportTransformed -> policeReportTransformed.getIdToOrigin().equals(idToOrigin))
                .findFirst();
    }
}
