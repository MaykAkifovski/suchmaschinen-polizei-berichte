package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.exception.NotFoundException;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.ComputeSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetDetailedSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response.GetSearchResponse;
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
@CrossOrigin
@RequestMapping("/policeReports")
public class PoliceReportController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ComputeSearchResponse computeSearch(@RequestBody FrontEndRequest frontEndRequest) {
        return searchService.computeSearch(frontEndRequest);
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetSearchResponse> getSearch(@RequestParam("searchId") String searchId,
                                             @RequestParam("page") int page,
                                             @RequestParam("pageSize") int pageSize) {
        try {
            return searchService.getSearch(searchId, page, pageSize);
        } catch (NotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }
    }

    @RequestMapping(value = "{id}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public GetDetailedSearchResponse getOnePoliceReport(@PathVariable String id) {
        try {
            return searchService.getPoliceReportById(id);
        } catch (NotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }
    }

}
