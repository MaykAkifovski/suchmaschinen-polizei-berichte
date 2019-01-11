package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ComputeSearchResponse {
    String searchId;
    int count;
}
