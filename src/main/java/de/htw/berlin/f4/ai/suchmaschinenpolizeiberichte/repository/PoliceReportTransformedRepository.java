package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PoliceReportTransformedRepository extends MongoRepository<PoliceReportTransformed, String> {
}
