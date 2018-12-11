package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services.PoliceReportTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private PoliceReportTransformer policeReportTransformer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        policeReportTransformer.run();
    }
}
