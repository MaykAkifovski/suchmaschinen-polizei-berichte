package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.lemmatizer;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class LemmatizerApiResponse {

    private String word2;
    private String relation;
}
