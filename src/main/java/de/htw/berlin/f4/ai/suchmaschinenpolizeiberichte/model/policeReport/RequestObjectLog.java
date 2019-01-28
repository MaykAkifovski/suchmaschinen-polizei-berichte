package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RankedPoliceReport;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "RequestObjectLog")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RequestObjectLog {
    @Id
    public String id;

    public String date;
    public String searchedText;
    public List<String> searchedLocation;
    public List<String> searchedDatum;
    public List<RankedPoliceReport> results;

}
