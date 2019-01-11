package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  @Value("${swaggerui.apiInfo.title}")
  private String swaggeruiApiInfoTitle;

  @Bean
  public Docket technischeDokumentation() {
    return new Docket(SWAGGER_2)
        .select()
            .apis(RequestHandlerSelectors.basePackage("de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.controllers"))
        .paths(PathSelectors.any())
        .build()
        .pathMapping("/")
        .apiInfo(metadata());
  }

  private ApiInfo metadata() {
    return new ApiInfoBuilder()
        .title(swaggeruiApiInfoTitle)
        .build();
  }
}
