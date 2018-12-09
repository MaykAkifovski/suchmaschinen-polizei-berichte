package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportRepository;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PoliceReportTransformer {

    @Autowired
    private PoliceReportRepository policeReportRepository;
    @Autowired
    private PoliceReportTransformedRepository policeReportTransformedRepository;

    private List<String> filter;

    private void run() throws IOException {
        filter = initFilter();

        List<PoliceReport> alleBerichte = policeReportRepository.findAll();

        List<PoliceReportTransformed> tokanizierteBerichte = alleBerichte
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());

        policeReportTransformedRepository.saveAll(tokanizierteBerichte);
    }

    private List<String> initFilter() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get("D:\\HTWBerlin\\Semester5\\SuchMaschinen\\suchmaschinen-polizei-berichte\\src\\main\\resources\\stopwords.txt"))) {
            return stream.collect(Collectors.toList());
        }
    }

    private PoliceReportTransformed transform(PoliceReport policeReport) {
        List<String> contentTokenized = tokenize(policeReport.Content);
        List<String> titleTokenized = tokenize(policeReport.Title);
        return new PoliceReportTransformed(policeReport, contentTokenized, titleTokenized);
    }

    private List<String> tokenize(String toSplit) {
        return edit(toSplit.split(" "));
    }

    private List<String> edit(String[] splited) {
        List<String> edited = new ArrayList<>();
        edited.add(splited[0]);
        for (int i = 1; i < splited.length - 1; i++) {
            if (splited[i].equals("von")) {
                edited.add(splited[i - 1] + splited[i] + splited[i + 1]);
                i++;
            } else {
                edited.add(splited[i]);
            }
        }
        edited.add(splited[splited.length - 1]);
        return filter(edited);
    }

    private List<String> filter(List<String> edited) {
        return edited.stream()
                .filter(word -> !filter.contains(word))
                .collect(Collectors.toList());
    }

}