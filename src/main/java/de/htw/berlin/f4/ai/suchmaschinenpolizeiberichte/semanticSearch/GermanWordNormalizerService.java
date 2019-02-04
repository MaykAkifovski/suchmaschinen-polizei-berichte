package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.lemmatizer.Lemmatizer;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.lemmatizer.LemmatizerApiResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.LemmatizerLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GermanWordNormalizerService {

    private static final String URI = "http://api.corpora.uni-leipzig.de/ws/words/deu_news_2012_1M/wordrelations/";
    @Autowired
    private LemmatizerLoader lemmatizerLoader;

    private Map<String, String> lemmatizer = new HashMap<>();

    @PostConstruct
    public void init() {
        lemmatizerLoader.getLemmatizerList().forEach(o -> lemmatizer.put(o.getWord(), o.getLemmatizedWord()));
    }

    @Cacheable("normalizerCache")
    public String lemmatize(String word) {
        word = word.replaceAll("([,.!?]$)", "");
        return lemmatizer.getOrDefault(word, findWord(word));
    }

    private String findWord(String word) {
        String lemmatizedWord = normalize(getWordFromApi(word));
        lemmatizer.put(word, lemmatizedWord);
        lemmatizerLoader.add(Lemmatizer.builder().word(word).lemmatizedWord(lemmatizedWord).build());
        return lemmatizedWord;
    }

    private String getWordFromApi(String word) {
        try {
            ResponseEntity<List<LemmatizerApiResponse>> responseEntity = new RestTemplate()
                    .exchange(
                            URI + word,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<LemmatizerApiResponse>>() {
                            }
                    );
            List<LemmatizerApiResponse> responseBody = responseEntity.getBody();
            return responseBody == null || responseBody.isEmpty() || responseBody.get(0).getRelation().equals("is_baseform")
                    ? word : responseBody.get(0).getWord2();
        } catch (Exception ignored) {
            return word;
        }
    }

    private String normalize(String word) {
        return word
                .toLowerCase()
                .replaceAll("ß", "ss")
                .replaceAll("[Ää]", "a")
                .replaceAll("[Öö]", "o")
                .replaceAll("[Üü]", "u")
                .replaceAll("ae", "a")
                .replaceAll("oe", "o")
                .replaceAll("ue", "u");
    }
}
