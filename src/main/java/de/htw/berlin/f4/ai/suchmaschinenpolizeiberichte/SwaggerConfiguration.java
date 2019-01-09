@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  @Value("${swaggerui.apiInfo.title}")
  private String swaggeruiApiInfoTitle;

  @Value("${swaggerui.apiInfo.contact}")
  private String swaggeruiApiInfoContact;

  @Value("${swaggerui.apiInfo.version}")
  private String swaggeruiApiInfoVersion;

  @Value("${application.apiVersion}")
  private String apiVersion;

  @Bean
  public Docket technischeDokumentation() {
    return new Docket(SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("de.europace.pep.product.cockpit.unterlagen.controller.unterlagen"))
        .paths(PathSelectors.any())
        .build()
        .pathMapping("/")
        .apiInfo(metadata());
  }

  private ApiInfo metadata() {
    return new ApiInfoBuilder()
        .title(swaggeruiApiInfoTitle)
        .version(swaggeruiApiInfoVersion)
        .contact(new Contact("", "", swaggeruiApiInfoContact))
        .build();
  }
}
