package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.exception.NotFoundException;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.ComputeSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetDetailedSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch.AutocompleteService;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.semanticSearch.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/autocomplete")
public class AutocompleteController {

    @Autowired
    private AutocompleteService autocompleteService;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getSearch(@RequestParam("sb") String subString, @RequestParam("size") int n) {
        return autocompleteService.getSuggestions(subString, n);
    }


}
