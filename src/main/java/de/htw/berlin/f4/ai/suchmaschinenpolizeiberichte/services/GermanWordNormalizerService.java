package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.ApiResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.TransformedWordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GermanWordNormalizerService {

    private static final String URI = "http://api.corpora.uni-leipzig.de/ws/words/deu_news_2012_1M/wordrelations/";

    @Autowired
    private TransformedWordsRepository transformedWordsRepository;

    @Cacheable("normalizerCache")
    public String normalize(String word) {
        word = word.replaceAll("([,.!?]$)", "");
        String wordFromApi = getWordFromApi(word);
        String transformedWord = transform(wordFromApi);
        saveTransformedWord(word, transformedWord);
        return transformedWord;
    }

    private String getWordFromApi(String word) {
        try {
            ResponseEntity<List<ApiResponse>> responseEntity = new RestTemplate()
                    .exchange(
                            URI + word,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<ApiResponse>>() {
                            }
                    );
            List<ApiResponse> responseBody = responseEntity.getBody();
            return responseBody == null || responseBody.isEmpty() || responseBody.get(0).getRelation().equals("is_baseform")
                    ? word : responseBody.get(0).getWord2();
        } catch (Exception ignored) {
            return word;
        }
    }

    private String transform(String word) {
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

    private void saveTransformedWord(String word, String transformedWord) {
//        transformedWordsRepository.save(new TransformedWord(word, transformedWord));
    }
}
