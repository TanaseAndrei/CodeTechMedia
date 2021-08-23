package com.ucv.media.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String SWAGGER_TITLE = "CodeTechMedia API Swagger documentation";
    private static final String SWAGGER_DESCRIPTION = "This represents the application's api.";
    private static final String SWAGGER_LICENSE = "Apache License Version 2.0, January 2004";
    private static final String SWAGGER_LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0.txt";

    @Value("${application.version}")
    private String applicationVersion;

    @Bean
    public Docket apiDocumentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ucv.media.controller"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(this.apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(getTitle(), getDescription(), applicationVersion, null, getContact(),
                getLicense(), getLicenseUrl(), Collections.emptyList());
    }

    private String getTitle() {
        return SWAGGER_TITLE;
    }

    private String getDescription() {
        return SWAGGER_DESCRIPTION;
    }

    private Contact getContact() {
        return new Contact("Andrei-Cristian Tanase", null, "stonixandrei@yahoo.com," +
                " tanase.andrei.b5f@student.ucv.ro");
    }

    private String getLicense() {
        return SWAGGER_LICENSE;
    }

    private String getLicenseUrl() {
        return SWAGGER_LICENSE_URL;
    }
}
