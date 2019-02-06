package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

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

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getSearch(@RequestParam("sb") String subString, @RequestParam("size") int n) {
        List<String> tokenized = new LinkedList<>(Arrays.asList(subString.split("\\s")));

        if (tokenized.size() == 0) {
            return Collections.emptyList();
        } else if (subString.substring(subString.length() - 1).equals(" ")) {
            //----->  HIER DEIN CODE   <------
            return Collections.emptyList();
        } else if (tokenized.size() == 1) {
            return autocompleteService.getSuggestions(subString, n);
        } else {
            List<String> results = autocompleteService
                    .getSuggestions(tokenized.get(tokenized.size() - 1), n);
            tokenized.remove(tokenized.size() - 1);

            return results.stream()
                    .map(x -> tokenized.stream()
                            .collect(Collectors.joining(" ")) + " " + x)
                    .collect(Collectors.toList());
        }
    }
}
