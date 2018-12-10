package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PoliceReports")
public class PoliceReport {
    @Id
    public String id;

    public String header;
    public Boolean isLocationInHeader;
    public String title;
    public String url;
    public String createdAt;
    public String content;

    public PoliceReport() {
    }

    public PoliceReport(String id, String header, Boolean isLocationInHeader, String title, String url, String createdAt, String content) {
        this.id = id;
        this.header = header;
        this.isLocationInHeader = isLocationInHeader;
        this.title = title;
        this.url = url;
        this.createdAt = createdAt;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public Boolean getLocationInHeader() {
        return isLocationInHeader;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setLocationInHeader(Boolean locationInHeader) {
        isLocationInHeader = locationInHeader;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
