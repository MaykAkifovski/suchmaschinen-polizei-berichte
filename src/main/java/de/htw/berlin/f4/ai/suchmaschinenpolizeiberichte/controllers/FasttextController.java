package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.FasttextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin
@RequestMapping("/fastTextController")
public class FasttextController {

    @Autowired
    private FasttextLoader fasttextLoader;

    @RequestMapping(method = GET)
    public List<Map.Entry<String, Double>> getSuggestions(
            @RequestParam(value = "word", defaultValue = "Auto") String word) {
        return fasttextLoader.cosinusSimilarity(word);
    }
}
