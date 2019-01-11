package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ComputeSearchResponse {
    private String searchId;
    private int count;
}
