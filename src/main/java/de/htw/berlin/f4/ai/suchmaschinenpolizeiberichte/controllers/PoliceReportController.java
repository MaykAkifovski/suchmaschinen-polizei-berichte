package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.RankedPoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services.PoliceReportsRanker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/policeReports")
public class PoliceReportController {

    @Autowired
    private PoliceReportsRanker policeReportsRanker;

    @RequestMapping(method = POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RankedPoliceReport> getTop10PoliceReports(@RequestBody FrontEndRequest frontEndRequest) {
        return policeReportsRanker.getTop10PoliceReports(frontEndRequest);
    }
}
