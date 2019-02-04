package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.FasttextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/dbTaskController")
public class DbTaskController {

    @Autowired
    private FasttextLoader fasttextLoader;

    @RequestMapping(method = GET)
    public String runTask() {
        return fasttextLoader.cosinusSimilarity("auto");
    }

}
