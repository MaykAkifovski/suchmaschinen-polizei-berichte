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
    private List<String> searchDateRange;
    private List<String> searchLocations;
}
