package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PoliceReports")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PoliceReport {
    private String _id;
    private String header;
    private Boolean isLocationInHeader;
    private String title;
    private String url;
    private String createdAt;
    private String content;
    private Object _class;
}
