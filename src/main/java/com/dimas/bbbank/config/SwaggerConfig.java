package com.dimas.bbbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).pathMapping("/api/v1").select()
                .apis(or(RequestHandlerSelectors.basePackage("com.dimas.bbbank"), RequestHandlerSelectors.basePackage("com.dimas.bbbank"))).paths(PathSelectors.any()).build();
    }
}
