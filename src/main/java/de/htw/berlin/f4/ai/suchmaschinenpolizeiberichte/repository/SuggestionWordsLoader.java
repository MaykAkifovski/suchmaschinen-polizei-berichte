package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;


import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.Tries.Trie;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Component
public class SuggestionWordsLoader {
    private Map<String, Integer> suggestionWords = new HashMap<>();
    private Trie trie = new Trie();

    @PostConstruct
    public void init() {
        try {
            InputStream fis = getClass().getClassLoader().getResourceAsStream("suggestionWords2.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            suggestionWords = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found for Deserialization");
            c.printStackTrace();
            return;
        }
        System.out.println("Deserialized HashMap..");

        lastCleaning("e");
        lastCleaning("en");
        lastCleaning("nen");
        lastCleaning("n");
        lastCleaning("es");
        lastCleaning("s");
        System.out.println(suggestionWords.size());

        suggestionWords.entrySet().forEach(x -> {
            trie.insert(x.getKey(), x.getValue());
        });
        System.out.println("Trie loaded..");
        suggestionWords = null;
    }

private void lastCleaning(String wordEnd) {
    Map<String, Integer> results = new HashMap<>(suggestionWords);
    for (Map.Entry<String, Integer> entry : results.entrySet()) {
        String singular = entry.getKey();
        if (suggestionWords.containsKey(singular + wordEnd) && suggestionWords.containsKey(singular)) {
            suggestionWords.put(singular, suggestionWords.get(singular+wordEnd) + suggestionWords.get(singular));
            suggestionWords.remove(singular + wordEnd);
        }
    }
}
    public Trie getTrie() {
        return trie;
    }
}