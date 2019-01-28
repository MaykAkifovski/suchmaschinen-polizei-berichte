package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "PoliceReportsTransformed_2")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class PoliceReportTransformed {
    @Id
    private String id;
    private String idToOrigin;
    private String location;
    private long date;
    private List<String> title;
    private List<String> content;
    private List<String> synonymsForContent;
}
