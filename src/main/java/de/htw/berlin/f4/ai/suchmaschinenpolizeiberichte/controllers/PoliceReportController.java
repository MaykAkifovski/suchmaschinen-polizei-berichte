package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.ComputeSearchResponse;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
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
    public List<RankedPoliceReport> getSearch(@RequestParam("searcId") String searcId,
                                              @RequestParam("page") int page,
                                              @RequestParam("pageSize") int pageSize) {
        return searchService.getSearch(searcId, page, pageSize);
    }


}
