package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.lemmatizer.Lemmatizer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class LemmatizerLoader {
    private List<Lemmatizer> lemmatizerList = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper om = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/lemmatizer.json");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                lemmatizerList.add(om.readValue(line, Lemmatizer.class));
            }
        }
    }

    public List<Lemmatizer> getLemmatizerList() {
        return lemmatizerList;
    }

    public void add(Lemmatizer lemmatizer) {
        lemmatizerList.add(lemmatizer);
    }
}
