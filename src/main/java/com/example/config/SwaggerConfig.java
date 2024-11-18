package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("info@company.az");
        contact.setName("Company Name");
        contact.setUrl("https://www.company.az");

        Info info = new Info()
                .title("MS-SPRING-BOOT")
                .version("1.0")
                .contact(contact)
                .description("This API facilitates your workload")
                .termsOfService("https://www.company.az");

        return new OpenAPI().info(info);
    }
}