package com.jobtrack.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * OpenAPI 3 / Swagger Configuration.
 * Configures API documentation, interactive Swagger UI metadata, and JWT Bearer security scheme.
 */
@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JobTrack REST API")
                        .version("1.0.0")
                        .description("Production-ready Full-Stack Job Application Tracker & Career Pipeline Analytics Engine.\n\n" +
                                "### Features:\n" +
                                "- **Authentication & Security**: Stateless JWT authentication with BCrypt password hashing\n" +
                                "- **Job Applications**: Multi-tenant CRUD operations with filtering, sorting, and user isolation\n" +
                                "- **Career Analytics**: Real-time conversion funnel, interview rates, and salary metrics\n" +
                                "- **Data Export**: User-scoped CSV report generation\n\n" +
                                "**Note:** For protected endpoints, authenticate via `/api/v1/auth/login` or `/api/v1/auth/register`, copy the `accessToken`, click **Authorize** at the top right, and paste the token.")
                        .contact(new Contact()
                                .name("JobTrack Engineering Team")
                                .email("developer@jobtrack.com")
                                .url("https://github.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ))
                .tags(Arrays.asList(
                        new Tag().name("Authentication").description("User registration, authentication login, and profile operations"),
                        new Tag().name("Job Applications").description("Full CRUD operations, search, filtering, and status progression for job applications"),
                        new Tag().name("Analytics & Insights").description("Career pipeline metrics, conversion rates, and salary intelligence")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT Bearer token format: `eyJhbGciOi...` (without 'Bearer ' prefix)")));
    }
}
