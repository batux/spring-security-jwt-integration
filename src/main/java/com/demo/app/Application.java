package com.demo.app;

import com.demo.app.security.config.ApiSecurityConfig;
import com.demo.app.swagger.Swagger2Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@Import( { ApiSecurityConfig.class, Swagger2Configuration.class } )
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
