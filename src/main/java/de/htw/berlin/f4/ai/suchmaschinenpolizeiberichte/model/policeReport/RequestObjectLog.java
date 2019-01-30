package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "RequestObjectLog")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RequestObjectLog {
    private String id;
    private String date;
    private String searchedText;
    private List<String> searchedLocation;
    private List<String> searchedDatum;
    private List<RankedPoliceReport> results;
}
