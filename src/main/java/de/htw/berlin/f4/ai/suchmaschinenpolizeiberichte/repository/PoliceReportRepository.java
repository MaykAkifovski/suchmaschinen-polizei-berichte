package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PoliceReportRepository extends MongoRepository<PoliceReport, String> {
    List<PoliceReport> getByIsLocationInHeader(boolean isLocationInHeader);
}
