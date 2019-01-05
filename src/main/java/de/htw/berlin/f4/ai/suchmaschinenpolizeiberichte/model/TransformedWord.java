package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TransformedWord")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TransformedWord {
    private String word;
    private String transformedWord;
}
