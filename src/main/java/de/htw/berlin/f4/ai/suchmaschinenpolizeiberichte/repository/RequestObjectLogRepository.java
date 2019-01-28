package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RequestObjectLog;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RequestObjectLogRepository extends MongoRepository<RequestObjectLog, String> {

}
