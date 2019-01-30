package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport;

import lombok.*;

import java.util.List;

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
}
