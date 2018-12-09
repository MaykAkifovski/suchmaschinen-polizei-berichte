package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.PoliceReportRepository;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services.PoliceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private PoliceReportRepository policeReportRepository;
    @Autowired
    private PoliceReportService policeReportService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Start deleting");
        policeReportRepository.deleteAll();
        System.out.println("Finished deleting");
        List<PoliceReport> policeReports = policeReportService.readPolizeiBerichteFromFile();
        System.out.println("Start saving");
        List<PoliceReport> policeReportsSaved = policeReportRepository.saveAll(policeReports);
        System.out.println("Finished saving");
    }
}
