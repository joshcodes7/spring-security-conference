package com.example.EmployeeManagementApplication.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@SecurityScheme(
        name = "BearerAuth",
        description = "JWT auth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("controller1")
                .apiInfo(apiInfo()).select()
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket Api() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("controller2")
                .apiInfo(apiInfo()).select()
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Employee Management Service")
                .description("The Employee Project Management system is a web-based application designed " +
                        "to streamline the process of assigning and managing projects within an organization.")
                .termsOfServiceUrl("https://github.com/joshcodes7/Employee-Project-Management")
                .licenseUrl("joshna.salu@people10.com")
                .version("3.0")
                .build();
    }


}





//@OpenAPIDefinition(
//        info = @Info(
//                title = "Code-First Approach (reflectoring.io)",
//                description = "" +
//                        "Lorem ipsum dolor ...",
//                contact = @Contact(
//                        name = "Reflectoring",
//                        url = "https://reflectoring.io",
//                        email = "petros.stergioulas94@gmail.com"
//                ),
//                license = @License(
//                        name = "MIT Licence",
//                        url = "https://github.com/thombergs/code-examples/blob/master/LICENSE")),
//        servers = @Server(url = "http://localhost:8080")
//)