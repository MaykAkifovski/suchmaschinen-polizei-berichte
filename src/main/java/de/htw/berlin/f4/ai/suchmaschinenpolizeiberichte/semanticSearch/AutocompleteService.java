package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.SuggestionWordsLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AutocompleteService {

    @Autowired
    private SuggestionWordsLoader suggestionWordsLoader;


    public List<String> getSuggestions(String sb, int n) {
        try {
            List<String> suggestions = getTopNFSortedsuggestions(sb.toLowerCase(), n);
            if (suggestions.size() == 0) {
                suggestions = getTopNFSortedsuggestionsWithLevensthein(sb.toLowerCase(), n);
            }
            return suggestions;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> getTopNFSortedsuggestions(String sb, int n) {

        if (sb.length() == 0) {
            return Collections.emptyList();
        }

        try {
            Map<String, Integer> predictedWords = suggestionWordsLoader.getTrie().getSuggestions(sb);
            return predictedWords.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(n)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> getTopNFSortedsuggestionsWithLevensthein(String sb, int n) {
        Map<String, Integer> predictedWords = suggestionWordsLoader.getTrie().getSuggestions(sb.substring(0, 2));
        List<String> predictedWordsList = new ArrayList<>(predictedWords.keySet());

        String candidate = findCandidate(predictedWordsList, sb, 2);
        if (candidate.length() == 0) {
            candidate = findCandidate(predictedWordsList, sb, 3);
        }
        return getTopNFSortedsuggestions(candidate, n);
    }

    private String findCandidate(List<String> predictedWordsList, String sb, int levDis) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        String candidate = "";
        for (String bar : predictedWordsList) {
            bar = StringUtils.substring(bar, 0, sb.length());

            if (levenshteinDistance.apply(bar, sb) < levDis) {
                candidate = bar;
                break;
            }
        }
        return candidate;
    }

}
