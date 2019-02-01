package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class TfIdfPoliceReport {
    private String idToPoliceReportTransformed;
    private Map<String, Double> tf_idf;
}
