package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReport;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.PoliceReportTransformed;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PoliceReportTransformedRepository extends MongoRepository<PoliceReportTransformed, String> {
    PoliceReportTransformed findOneByIdToOrigin(String IdToOrigin);
}
