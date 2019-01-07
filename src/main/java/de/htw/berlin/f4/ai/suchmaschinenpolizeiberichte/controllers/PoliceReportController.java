package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services.PoliceReportTransformer;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services.PoliceReportsRanker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/policeReports")
public class PoliceReportController {

    @Autowired
    private PoliceReportTransformer policeReportTransformer;
    @Autowired
    private PoliceReportsRanker policeReportsRanker;


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

    @RequestMapping(method = GET)
//    public List<String> getTop10PoliceReports(@RequestBody FrontEndRequest frontEndRequest) {
    public List<String> getTop10PoliceReports() {
        FrontEndRequest request = new FrontEndRequest();
        request.setLocation(Arrays.asList("Schöneberg", "Neukölln"));
        request.setStartDate("1419807600000");
        request.setEndDate("1419807600999");
        return policeReportsRanker.getTop10PoliceReports(request);
    }
}
