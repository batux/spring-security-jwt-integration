package com.demo.app.swagger;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Order(2)
public class Swagger2Configuration {

	@Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiEndPointsInfo())
        		.securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
        		.select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.demo.app.controller"))
                
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiEndPointsInfo() {

        return new ApiInfoBuilder().title("Demo REST API Layer")
                .description("Demo REST API Layer")
                .contact(new Contact("Batuhan Düzgün", "www.demo.com", "batuhan.duzgun@windowslive.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0")
                .build();
    }
    
    private ApiKey apiKey() { 
    	
        return new ApiKey("JWT", "Authorization", "header"); 
    }
    
    private SecurityContext securityContext() { 
    	
        return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
    } 

    private List<SecurityReference> defaultAuth() { 
    	
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
        authorizationScopes[0] = authorizationScope; 
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
    }
}
