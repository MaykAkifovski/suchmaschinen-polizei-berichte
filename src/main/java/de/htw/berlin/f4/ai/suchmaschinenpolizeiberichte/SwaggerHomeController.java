@Controller
public class SwaggerHomeController {

  @RequestMapping("/info")
  public String home() {
    return "redirect:swagger-ui.html#/";
  }
}
