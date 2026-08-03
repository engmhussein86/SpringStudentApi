package com.student.demo.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



    @Configuration
    public class OpenApiConfig {

        @Bean
        public OpenAPI studentApi() {

            return new OpenAPI()
                    .info(new Info()
                            .title("Student Management API")
                            .description("REST API for managing students using Spring Boot")
                            .version("1.0.0")
                            .contact(new Contact()
                                    .name("Eng. Mariam Hussein")
                                    .email("engmhussein86@gmail.com")));
        }
}
