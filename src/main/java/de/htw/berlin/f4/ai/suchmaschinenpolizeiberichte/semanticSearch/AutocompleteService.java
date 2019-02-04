package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.SuggestionWordsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AutocompleteService {

    @Autowired
    private SuggestionWordsLoader suggestionWordsLoader;


    public List<String> getSuggestions(String sb, int n) {
        return getTopNFSortedsuggestions(sb.toLowerCase(), n);
    }

    private List<String> getTopNFSortedsuggestions(String sb, int n){
        try{
            Map<String,Integer> predictedWords =  suggestionWordsLoader.getTrie().getSuggestions(sb);
            Map<String,Integer> sorted = predictedWords.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .limit(n)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue));

            return new ArrayList(sorted.keySet());
        } catch(Exception e){
            return Collections.emptyList();
        }

    }
}
