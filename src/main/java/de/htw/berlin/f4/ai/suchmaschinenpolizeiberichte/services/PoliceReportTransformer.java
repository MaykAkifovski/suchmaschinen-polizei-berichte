package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportRepository;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class PoliceReportTransformer {

    @Autowired
    private PoliceReportRepository policeReportRepository;
    @Autowired
    private PoliceReportTransformedRepository policeReportTransformedRepository;

    private List<String> filter;

    public void run() throws IOException {
        filter = initFilter();

        List<PoliceReport> policeReports = policeReportRepository.getByIsLocationInHeader(true);

        List<PoliceReportTransformed> tokanizierteBerichte = policeReports
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());

//        policeReportTransformedRepository.saveAll(tokanizierteBerichte);
        System.out.println();
    }

    private List<String> initFilter() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get("D:\\HTWBerlin\\Semester5\\SuchMaschinen\\suchmaschinen-polizei-berichte\\src\\main\\resources\\stopwords.txt"))) {
            return stream.collect(Collectors.toList());
        }
    }

    private PoliceReportTransformed transform(PoliceReport policeReport) {
        PoliceReportTransformed policeReportTransformed = new PoliceReportTransformed();
        policeReportTransformed.setIdToOrigin(policeReport.id);
        policeReportTransformed.setContent(tokenize(policeReport.content));
        extractDateLocation(policeReport.header, policeReportTransformed);
        return policeReportTransformed;
    }

    private void extractDateLocation(String header, PoliceReportTransformed policeReportTransformed) {
        String[] s = header.split(" ");
        System.out.println();

    }

    private List<String> tokenize(String toSplit) {
        return Arrays.stream(toSplit.split(" "))
                .map(this::transformWord)
                .filter(word -> !filter.contains(word) || !isEmpty(word))
                .collect(Collectors.toList());
    }

    private String transformWord(String word) {
        return word.toLowerCase().replaceAll("([,.!?]$)", "");
    }
}