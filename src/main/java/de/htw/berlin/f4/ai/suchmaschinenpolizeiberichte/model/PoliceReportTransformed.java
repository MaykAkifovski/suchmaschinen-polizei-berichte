package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PoliceReportsTransformed")
public class PoliceReportTransformed {
    private String idToOrigin;
}
