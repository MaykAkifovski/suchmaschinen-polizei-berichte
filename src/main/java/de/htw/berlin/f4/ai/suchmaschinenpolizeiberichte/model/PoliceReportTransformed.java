package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "PoliceReportsTransformed")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PoliceReportTransformed {
    @Id
    private String id;
    private String idToOrigin;
    private List<String> location;
    private long date;
    private List<String> title;
    private List<String> content;
}
