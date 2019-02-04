package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.exception.NotFoundException;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RequestObjectLog;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.ComputeSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetDetailedSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.RequestObjectLogLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.SuggestionWordsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
