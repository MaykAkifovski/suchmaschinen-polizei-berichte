package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services.PoliceReportTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/policeReports")
public class PoliceReportController {

    @Autowired
    private PoliceReportTransformer policeReportTransformer;

    @RequestMapping(value = "/{start}/{stop}", method = GET)
    @ResponseBody
    public String runTransformation(@PathVariable("start") Integer start, @PathVariable("stop") Integer stop) {
        try {
            policeReportTransformer.run(start, stop);
            return "Finished";
        } catch (IOException e) {
            return Arrays.toString(e.getStackTrace());
        }
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<String> getTop10PoliceReports(@RequestBody FrontEndRequest frontEndRequest) {
//        policeReportTransformer.
        return Collections.emptyList();
    }
}
