package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Lemmatizer")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Lemmatizer {
    private String word;
    private String lemmatizedWord;
}
