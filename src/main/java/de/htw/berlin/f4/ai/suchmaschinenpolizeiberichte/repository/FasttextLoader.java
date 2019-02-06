package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Getter
public class FasttextLoader {

    private Map<String, Double[]> fasttextModelRaw;

    @PostConstruct
    public void init() throws IOException {
        fasttextModelRaw = readFromFile("/model.json");
    }

    private Map<String, Double[]> readFromFile(String fileName) throws IOException {

        StringBuilder json = new StringBuilder();
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
        }
        Set<String> existing = new HashSet<>();
        return ((Map<String, Double[]>) new ObjectMapper().readValue(json.toString(), new TypeReference<Map<String, Double[]>>() {
        }))
                .entrySet()
                .stream()
                .filter(entry -> existing.add(normalize(entry.getKey())))
                .collect(Collectors.toMap(entry -> normalize(entry.getKey()), Map.Entry::getValue));
    }

    private String normalize(String word) {
        return word.replaceAll("([-,.!?]$)", "");
    }

    public List<Map.Entry<String, Double>> cosinusSimilarity(String word) {
        if (fasttextModelRaw.containsKey(word)) {
            Double[] searchVector = fasttextModelRaw.get(word);
            return fasttextModelRaw
                    .entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(word))
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> calculateCosinusSimilarity(searchVector, e.getValue())))
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() > 0.8)
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private Double calculateCosinusSimilarity(Double[] searchVector, Double[] _vector) {
        double sum = IntStream.range(0, searchVector.length)
                .parallel()
                .mapToDouble(id -> searchVector[id] * _vector[id])
                .reduce(0, Double::sum);

        Double a_skalar = Math.sqrt(IntStream.range(0, searchVector.length)
                .parallel()
                .mapToDouble(id -> searchVector[id] * searchVector[id])
                .reduce(0, Double::sum));

        Double b_skalar = Math.sqrt(IntStream.range(0, searchVector.length)
                .parallel()
                .mapToDouble(id -> _vector[id] * _vector[id])
                .reduce(0, Double::sum));

        return sum / (a_skalar * b_skalar);
    }
}
