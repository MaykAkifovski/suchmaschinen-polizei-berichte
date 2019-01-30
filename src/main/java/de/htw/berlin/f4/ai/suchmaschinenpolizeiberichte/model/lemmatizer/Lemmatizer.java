package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.lemmatizer;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Lemmatizer")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Lemmatizer {

    private Object _id;
    private String word;
    private String lemmatizedWord;
    private List<String> synonyms;
    private Object _class;
}
