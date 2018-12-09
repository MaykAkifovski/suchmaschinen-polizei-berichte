package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PoliceReports")
public class PoliceReport {
    @Id
    public String id;

    public String Header;
    public Boolean IsLocationInHeader;
    public String Title;
    public String URL;
    public String CreatedAt;
    public String Content;

    public PoliceReport() {

    }

    public PoliceReport(String id, String header, Boolean isLocationInHeader, String title, String URL, String createdAt, String content) {
        this.id = id;
        Header = header;
        IsLocationInHeader = isLocationInHeader;
        Title = title;
        this.URL = URL;
        CreatedAt = createdAt;
        Content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public void setLocationInHeader(Boolean locationInHeader) {
        IsLocationInHeader = locationInHeader;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public void setContent(String content) {
        Content = content;
    }
}
