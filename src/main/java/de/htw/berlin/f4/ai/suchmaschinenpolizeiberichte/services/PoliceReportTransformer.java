package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportRepository;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
    private HashMap<String, String> lexikon;

    public void run() throws IOException {
        filter = initFilter();
        initLexikon();

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

    private void initLexikon() throws IOException {
        lexikon = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\HTWBerlin\\Semester5\\SuchMaschinen\\suchmaschinen-polizei-berichte\\src\\main\\resources\\delexicon.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                String[] s = line.split("\t");
                String key = s[0].toLowerCase();
                String[] values = s[4].split(".");
                String value;
                if (values.length == 0) {
                    value = s[4].toLowerCase();
                } else {
                    value = values[values.length - 1].toLowerCase();
                }
                lexikon.put(key, value);
            }
        }
    }

    private PoliceReportTransformed transform(PoliceReport policeReport) {
        PoliceReportTransformed policeReportTransformed = new PoliceReportTransformed();
        policeReportTransformed.setIdToOrigin(policeReport.id);
        policeReportTransformed.setContent(tokenize(policeReport.content));
        policeReportTransformed.setTitle(tokenize(policeReport.title));
        extractDateLocation(policeReport.header, policeReportTransformed);
        return policeReportTransformed;
    }

    /**
     * pol_df["Location"] = pol_df["Header"].str.extract(
     * r"""(Mitte|Friedrichshain *- *Kreuzberg|Pankow|Charlottenburg* - *Wilmersdorf|Spandau|Steglitz* - *Zehlendorf|Tempelhof* - *Schöneberg|Neukölln|Treptow* - *Köpenick|Marzahn* - *Hellersdorf|Lichtenberg|Reinickendorf|bezirksübergreifend|berlinweit|bundesweit)""")
     * pol_df.loc[pol_df["Location"].isnull(),"Location"] = pol_df[pol_df["Location"].isnull()]["Content"].str.extract(
     * r"""(Mitte|Friedrichshain *- *Kreuzberg|Pankow|Charlottenburg* - *Wilmersdorf|Spandau|Steglitz* - *Zehlendorf|Tempelhof* - *Schöneberg|Neukölln|Treptow* - *Köpenick|Marzahn* - *Hellersdorf|Lichtenberg|Reinickendorf|bezirksübergreifend|[bB]erlinweit|Berlin/Brandenburg|Berlin/Köln|Berlin/Bayern|Berlin/Niedersachsen/Niederlande|bundesweit)""", expand=False)
     * pol_df["Location"] = pol_df["Location"].str.replace(" ","")
     *
     * @param header
     * @param policeReportTransformed
     */
    private void extractDateLocation(String header, PoliceReportTransformed policeReportTransformed) {
        String[] s = header.split(" ");
        System.out.println();

        List<String> location = Collections.emptyList();
        policeReportTransformed.setLocation(location);
        long date = 0;
        policeReportTransformed.setDate(date);
    }

    private List<String> tokenize(String toSplit) {
        return Arrays.stream(toSplit.split(" "))
                .map(this::transformWord)
                .filter(this::toFilter)
                .map(word -> lexikon.getOrDefault(word, word))
                .collect(Collectors.toList());
    }

    private boolean toFilter(String word) {
        return !(filter.contains(word) || isEmpty(word) || isNumber(word));
    }

    private boolean isNumber(String word) {
        try {
            Double.parseDouble(word);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private String transformWord(String word) {
        return word.toLowerCase().replaceAll("([,.!?]$)", "");
    }
}