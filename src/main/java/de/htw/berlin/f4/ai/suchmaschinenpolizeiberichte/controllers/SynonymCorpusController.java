package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.synonym.SynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/synonyms")
public class SynonymCorpusController {

    @Autowired
    private SynonymService synonymService;

    @RequestMapping(method = GET)
    public String run() {
        synonymService.run();
        return "Ready";
    }

}
