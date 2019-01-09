package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerHomeController {

  @RequestMapping("/info")
  public String home() {
    return "redirect:swagger-ui.html#/";
  }
}
