package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReport;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PoliceReportService {


//    @Autowired
//    private PoliceReportService policeReportService;

//        System.out.println("Start deleting");
//        policeReportRepository.deleteAll();
//        System.out.println("Finished deleting");
//        List<PoliceReport> policeReports = policeReportService.readPolizeiBerichteFromFile();
//        System.out.println("Start saving");
//        List<PoliceReport> policeReportsSaved = policeReportRepository.saveAll(policeReports);
//        System.out.println("Finished saving");

    public List<PoliceReport> readPolizeiBerichteFromFile() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String input = readFile();
        return objectMapper.readValue(input, new TypeReference<List<PoliceReport>>() {
        });
    }

    private String readFile() throws Exception {
        try (Stream<String> stream = Files.lines(Paths.get("D:\\HTWBerlin\\Semester5\\SuchMaschinen\\suchmaschinen-polizei-berichte\\src\\main\\resources\\policereports.json"))) {
            return stream.collect(Collectors.joining());
        }
    }
}
