package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import lombok.Getter;
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
@Getter
public class PoliceReportTransformedLoader {
    private List<PoliceReportTransformed> policeReportTransformedList = new ArrayList<>();

    private Supplier<Stream<String>> wholeCorpus;
    private Supplier<Stream<String>> uniqueCorpus;
    private Map<String, Double> idf;
    private Map<String, Double> tfIdf;

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
        policeReportTransformedList = computeTfIdfPoliceReports(policeReportTransformedList);
        tfIdf = computeTfIdf();
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

    private List<PoliceReportTransformed> computeTfIdfPoliceReports(List<PoliceReportTransformed> policeReportTransformedList) {
        return policeReportTransformedList
                .stream()
                .map(this::createTfIdfPoliceReport)
                .collect(Collectors.toList());
    }

    private PoliceReportTransformed createTfIdfPoliceReport(PoliceReportTransformed policeReportTransformed) {
        policeReportTransformed.setTf_idf(createTfIdf(policeReportTransformed));
        return policeReportTransformed;
    }

    private Map<String, Double> createTfIdf(PoliceReportTransformed policeReport) {
        return extractCorpus(policeReport)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() * idf.get(e.getKey())));
    }

    private Map<String, Double> computeTfIdf() {
        return wholeCorpus
                .get()
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() * idf.get(e.getKey())));

    }

    public Optional<PoliceReportTransformed> findOneByIdToOrigin(String idToOrigin) {
        return policeReportTransformedList
                .stream()
                .filter(policeReportTransformed -> policeReportTransformed.getIdToOrigin().equals(idToOrigin))
                .findFirst();
    }
}
