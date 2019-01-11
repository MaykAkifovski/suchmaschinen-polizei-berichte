package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.synonym;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.Lemmatizer;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.LemmatizerRepository;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services.GermanWordNormalizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SynonymService {

    @Autowired
    private GermanWordNormalizerService normalizerService;

    @Autowired
    private LemmatizerRepository lemmatizerRepository;


    private List<List<String>> synonyms = new ArrayList<>();
    private List<Lemmatizer> lemmatizers;

    public void run() {
        lemmatizers = lemmatizerRepository.findAll();
        readSynonyms();
        System.out.println();
    }

    private void readSynonyms() {
        InputStream inputStream = getClass().getResourceAsStream("/openthesaurus.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> synonymLine = Stream.of(line.split(";"))
                        .map(this::transform)
                        .collect(Collectors.toList());
                synonyms.add(synonymLine);
                addSynonymeToLemmatizer(synonymLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String transform(String word) {
        return normalizerService.lemmatize(word)
                .replaceAll("^([(].*[)]) ", "")
                .replaceAll(" ([(].*[)])$", "");
    }

    private void addSynonymeToLemmatizer(List<String> synonymLine) {
        for (Lemmatizer lemmatizer : lemmatizers) {
            if (synonymLine.contains(lemmatizer.getLemmatizedWord())) {
                lemmatizer.setSynonyms(synonymLine);
                lemmatizerRepository.save(lemmatizer);
            }
        }
    }
}
