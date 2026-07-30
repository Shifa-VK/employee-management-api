package com.shifa.employee_management_api.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI employeeManagementApi(){

        return new OpenAPI()

                .info(new Info()

                        .title("Employee Management API")

                        .description("REST API for managing employees")

                        .version("1.0.0")

                        .contact(new Contact()

                                .name("Shifa VK")

                                .email("test@gmail.com"))

                        .license(new License()

                                .name("Apache 2.0")))
                // Enable JWT
                .addSecurityItem(new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME))

                .schemaRequirement(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));

//                .externalDocs(new ExternalDocumentation()
//
//                        .description("Project Documentation")
//
//                        .url("https://github.com/your-github"));

    }
}
