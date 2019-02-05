package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/dbTaskController")
public class DbTaskController {

    @RequestMapping(method = GET)
    public String runTask() {
        return "";
    }

}
