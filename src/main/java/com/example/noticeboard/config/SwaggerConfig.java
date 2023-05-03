package com.example.noticeboard.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "게시판 api 명세서", description = "api 명세서", version = "v1"))
@Configuration
public class SwaggerConfig {

}
