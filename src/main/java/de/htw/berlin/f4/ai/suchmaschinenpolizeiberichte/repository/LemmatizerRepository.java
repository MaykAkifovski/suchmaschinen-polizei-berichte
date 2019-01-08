package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.Lemmatizer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LemmatizerRepository extends MongoRepository<Lemmatizer, String> {
}
