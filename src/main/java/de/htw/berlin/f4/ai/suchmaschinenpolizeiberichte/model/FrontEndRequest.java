package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FrontEndRequest {
    private List<String> location;
    private String startDate;
    private String endDate;
    private String text;
}
