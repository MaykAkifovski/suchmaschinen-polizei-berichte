package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "PoliceReportsTransformed")
public class PoliceReportTransformed {
    public PoliceReportTransformed(PoliceReport policeReport, List<String> contentTokenized, List<String> titleTokenized) {

    }
}
