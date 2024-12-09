package com.nurijang.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(
                        new Info().title("nurijang API")
                                .version("1.0.1")
                )
                .servers(Collections.singletonList(
                        new Server().url("https://api.nurijang.p-e.kr").description("Production server")
                ));
    }
}
