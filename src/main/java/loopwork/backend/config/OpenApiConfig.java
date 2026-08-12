package loopwork.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loopworkOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loopwork API")
                        .description("Scheduling system for self-employed professionals with recurring clients")
                        .version("v1"));
    }
}