package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class GetDetailedSearchResponse {
    private String header;
    private String title;
    private String url;
    private String createdAt;
    private String content;
}
