package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RankedPoliceReport {
    private String idToOrigin;
    private int score;
}
