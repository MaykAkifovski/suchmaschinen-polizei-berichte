package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
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
public class PoliceReportTransformedLoader {
    private List<PoliceReportTransformed> policeReportTransformedList = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper om = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/policeReportsTransformed.json");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                policeReportTransformedList.add(om.readValue(line, PoliceReportTransformed.class));
            }
        }
    }

    public List<PoliceReportTransformed> getPoliceReportTransformedList() {
        return policeReportTransformedList;
    }

    public Optional<PoliceReportTransformed> findOneByIdToOrigin(String idToOrigin) {
        return policeReportTransformedList
                .stream()
                .filter(policeReportTransformed -> policeReportTransformed.getIdToOrigin().equals(idToOrigin))
                .findFirst();
    }
}
