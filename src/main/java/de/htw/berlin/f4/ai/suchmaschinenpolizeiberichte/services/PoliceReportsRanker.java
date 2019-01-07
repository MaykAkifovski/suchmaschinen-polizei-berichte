package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.FrontEndRequest;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportTransformedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PoliceReportsRanker {

    @Autowired
    private PoliceReportTransformedRepository policeReportTransformedRepository;

    public List<String> getTop10PoliceReports(FrontEndRequest frontEndRequest) {

        List<PoliceReportTransformed> allReports = getAllReports(frontEndRequest);
        return null;
    }

    private List<PoliceReportTransformed> getAllReports(FrontEndRequest frontEndRequest) {
        return policeReportTransformedRepository
                .findAll()
                .stream()
                .filter(policeReportTransformed -> filterByLocation(policeReportTransformed.getLocation(), frontEndRequest.getLocation()))
                .filter(policeReportTransformed -> filterByDate(policeReportTransformed.getDate(), frontEndRequest.getStartDate(), frontEndRequest.getEndDate()))
                .collect(Collectors.toList());
    }

    private boolean filterByLocation(List<String> locationsInReport, List<String> locationsInRequest) {
        return locationsInRequest
                .stream()
                .anyMatch(locationsInReport::contains);
    }

    private boolean filterByDate(long date, String startDate, String endDate) {
        //            long sDate = new SimpleDateFormat("dd.MM.yyyy").parse(startDate).getTime();
//            long eDate = new SimpleDateFormat("dd.MM.yyyy").parse(endDate).getTime();
        try {
            return Long.parseLong(startDate) <= date && Long.parseLong(endDate) >= date;
        } catch (Exception ignored) {
            return false;
        }
    }

}
