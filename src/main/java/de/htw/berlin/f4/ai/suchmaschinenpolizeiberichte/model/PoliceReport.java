package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PoliceReports")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PoliceReport {
    @Id
    public String id;

    public String header;
    public Boolean isLocationInHeader;
    public String title;
    public String url;
    public String createdAt;
    public String content;

}
