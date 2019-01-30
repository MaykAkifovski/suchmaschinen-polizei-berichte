package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class GetSearchResponse {
    public String id;
    public String location;
    public long date;
    public String content;
}
