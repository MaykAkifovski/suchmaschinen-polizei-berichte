package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository.FasttextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/dbTaskController")
public class DbTaskController {

    @Autowired
    private FasttextLoader fasttextLoader;

    @RequestMapping(method = GET)
    public List<Map.Entry<String, Double>> runTask() {
        return fasttextLoader.cosinusSimilarity("Auto");
    }

}
