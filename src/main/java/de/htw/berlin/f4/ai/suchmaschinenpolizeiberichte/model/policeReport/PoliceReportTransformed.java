package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class PoliceReportTransformed {
    private String _id;
    private String idToOrigin;
    private String location;
    private long date;
    private List<String> title;
    private List<String> content;
    private List<String> synonymsForContent;
    private String _class;
    private Map<String, Double> tf_idf;
}
