package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class GetSearchResponse {
    private String id;
    private String location;
    private long date;
    private String content;
}
