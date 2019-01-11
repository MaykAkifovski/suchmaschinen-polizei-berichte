package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class TextTokenizer {

    @Autowired
    private GermanWordNormalizerService normalizerService;

    private List<String> stopWords = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/stopwords.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.add(line);
            }
        }
    }

    public List<String> transform(String toTransform) {
        return Arrays.stream(toTransform.split(" "))
                .filter(this::toFilter)
                .map(this::normalize)
                .collect(Collectors.toList());

    }

    private boolean toFilter(String word) {
        return !(stopWords.contains(word.toLowerCase()) || isEmpty(word));
    }

    private String normalize(String word) {
        return normalizerService.lemmatize(word);
    }
}
