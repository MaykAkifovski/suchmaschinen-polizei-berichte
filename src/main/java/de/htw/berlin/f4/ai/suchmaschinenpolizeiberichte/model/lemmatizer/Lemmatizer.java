package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.lemmatizer;

import lombok.*;

import java.util.List;

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
