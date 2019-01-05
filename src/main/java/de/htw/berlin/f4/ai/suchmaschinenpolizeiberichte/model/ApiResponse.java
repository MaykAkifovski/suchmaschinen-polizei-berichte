package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApiResponse {

    private String word2;
    private String relation;
}
