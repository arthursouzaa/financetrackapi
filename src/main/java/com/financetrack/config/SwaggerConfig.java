package com.financetrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
// import io.swagger.v3.oas.models.info.Contact;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                                .title("FinanceTrack API")
                                .description("API do FinanceTrack")
                                .version("1.0")

                        // Exemplo de contato
                        /*
                        .contact(new Contact()
                                .name("Marco Antônio Pereira Araújo")
                                .url("http://github.com/marcoaparaujo")
                                .email("marcoaparaujo@gmail.com"))
                        */
                );
    }
}