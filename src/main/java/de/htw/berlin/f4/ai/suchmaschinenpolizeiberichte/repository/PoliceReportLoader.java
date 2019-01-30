package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReport;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PoliceReportLoader {
    private List<PoliceReport> policeReportList = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper om = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/policeReports.json");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                policeReportList.add(om.readValue(line, PoliceReport.class));
            }
        }
    }

    public Optional<PoliceReport> findById(String searchId) {
        return policeReportList
                .stream()
                .filter(policeReport -> policeReport.get_id().equals(searchId))
                .findFirst();
    }
}
