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
    public void init()  {
        try
        {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("suggestionWords2.ser").getFile());
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            suggestionWords = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Class not found for Deserialization");
            c.printStackTrace();
            return;
        }
        System.out.println("Deserialized HashMap..");

        suggestionWords.entrySet().forEach(x -> {
            trie.insert(x.getKey(),x.getValue());
      });
        System.out.println("Trie loaded..");
        suggestionWords = null;
    }



    public Trie getTrie() {
        return trie;
    }

    }



