package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.FasttextLoader;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch.AutocompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin
@RequestMapping("/autocomplete")
public class AutocompleteController {

    @Autowired
    private AutocompleteService autocompleteService;
    @Autowired
    private FasttextLoader fasttextLoader;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getSearch(@RequestParam("sb") String subString, @RequestParam("size") int n) {
        LinkedList<String> tokenized = new LinkedList<>(Arrays.asList(subString.split("\\s")));

        if (tokenized.size() == 0) {
            return Collections.emptyList();
        } else if (subString.endsWith(" ")) {
            List<String> suggestions = fasttextLoader.runFasttext(tokenized.getLast());
            return suggestions.isEmpty() ? getSuggestionsForMoreTokens(n, tokenized) : appendSuggestionsForSecondWord(tokenized, suggestions);
        } else if (tokenized.size() == 1) {
            return autocompleteService.getSuggestions(subString, n);
        } else {
            return getSuggestionsForMoreTokens(n, tokenized);
        }
    }

    private List<String> getSuggestionsForMoreTokens(int n, LinkedList<String> tokenized) {
        List<String> results = autocompleteService.getSuggestions(tokenized.getLast(), n);
        return appendSuggestionsForSecondWord(tokenized, results);
    }

    private List<String> appendSuggestionsForSecondWord(LinkedList<String> tokenized, List<String> results) {
        tokenized.removeLast();
        return results.stream()
                .map(x -> String.join(" ", tokenized) + " " + x)
                .collect(Collectors.toList());
    }
}