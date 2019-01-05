package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.TransformedWord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransformedWordsRepository extends MongoRepository<TransformedWord, String> {
}
