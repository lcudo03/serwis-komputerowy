package pl.serwis.komputerowy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Serwis komputerowy API")
            .version("1.0.0")
            .description("REST API zgodne z dokumentacją projektu (Spring Boot + PostgreSQL)."));
  }
}
