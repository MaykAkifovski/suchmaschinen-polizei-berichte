package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FrontEndRequest {
    private String searchString;
    private List<String> searchDaterange;
    private List<String> searchLocations;
}
