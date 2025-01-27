package com.Omok.global;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer customHeaderOpenApiCustomizer() {
        return openApi -> {
            openApi.getPaths().forEach((path, pathItem) ->
                    pathItem.readOperations().forEach(operation -> {
                        Parameter customHeader = new HeaderParameter()
                                .name("Authorization")
                                .description("Global Header for all APIs")
                                .required(false)
                                .schema(new StringSchema());
                        operation.addParametersItem(customHeader);
                    })
            );
        };
    }
}
