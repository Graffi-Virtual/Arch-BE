package com.cd.arch.config

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License

@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Chat Application API")
                    .description("Chat Application with GPT integration")
                    .version("1.0.0")
                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
            )
            .externalDocs(ExternalDocumentation()
                .url("https://api.openai.com"))
    }
}