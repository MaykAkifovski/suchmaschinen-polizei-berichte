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
import java.util.ArrayList;
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
    @Autowired
    private GermanWordNormalizerService germanWordNormalizerService;

    private List<String> filter = new ArrayList<>();
    private List<PoliceReport> policeReports = new ArrayList<>();

    public void run(Integer start, Integer stop) throws IOException {
        if (filter.isEmpty())
            filter = initFilter();

        if (policeReports.isEmpty())
            policeReports = policeReportRepository.getByIsLocationInHeader(true);

        for (int i = start; i < stop; i++) {
            PoliceReport policeReport = policeReports.get(i);
            PoliceReportTransformed policeReportTransformed = transform(policeReport);
//            policeReportTransformedRepository.save(policeReportTransformed);
        }
//        List<PoliceReportTransformed> tokanizierteBerichte = policeReports
//                .stream()
//                .map(this::transform)
//                .collect(Collectors.toList());
//
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
        policeReportTransformed.setTitle(tokenize(policeReport.title));

//        extractDateLocation(policeReport.header, policeReportTransformed);
        return policeReportTransformed;
    }

//    protected void extractDateLocation(String header, PoliceReportTransformed policeReportTransformed) {
//        String[] headerStrings = header.replaceAll("[\n\r]", " ").split(" ");
//        Set<String> locations = new HashSet<>(Arrays.asList("Mitte", "Friedrichshain", "Kreuzberg", "Pankow", "Charlottenburg", "Wilmersdorf", "Spandau", "Steglitz", "Zehlendorf", "Tempelhof", "Schöneberg", "Neukölln", "Treptow", "Köpenick", "Marzahn", "Hellersdorf", "Lichtenberg", "Reinickendorf", "bezirksübergreifend", "berlinweit", "bundesweit"));
//        List<String> locationsFound = new ArrayList<>();
//
//        for (String headerElement : headerStrings) {
//            if (Character.isDigit(headerElement.charAt(0)) && headerElement.length() > 7) {
//                long date = 0;
//                try {
//                    date = new SimpleDateFormat("dd.MM.yyyy").parse(headerElement).getTime();
//                } catch (ParseException pe) {
//                    pe.printStackTrace();
//                }
//                policeReportTransformed.setDate(date);
//            } else if (locations.contains(headerElement)) {
//                locationsFound.add(headerElement);
//            }
//        }
//        policeReportTransformed.setLocation(locationsFound);
//    }

    private List<String> tokenize(String toSplit) {
        return Arrays.stream(toSplit.split(" "))
                .filter(this::toFilter)
                .map(this::normalize)
                .collect(Collectors.toList());
    }

    private boolean toFilter(String word) {
        return !(filter.contains(word.toLowerCase()) || isEmpty(word));
    }

    private String normalize(String word) {
        return germanWordNormalizerService.lemmatize(word);
    }
}