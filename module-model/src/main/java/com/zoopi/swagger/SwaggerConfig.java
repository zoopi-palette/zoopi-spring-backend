package com.zoopi.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.globalOperationParameters(globalOperation())
			.useDefaultResponseMessages(false)
			.apiInfo(apiInfo())
			.select()
			.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
			.paths(PathSelectors.any())
			.build();
	}

	private List<Parameter> globalOperation() {
		final List<Parameter> global = new ArrayList<>();
		global.add(new ParameterBuilder()
			.name("Authorization")
			.description("Access Token")
			.parameterType("header")
			.modelRef(new ModelRef("string"))
			.scalarExample("Bearer AAA.BBB.CCC")
			.build());
		return global;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("Zoopi's API Docs")
			.version("1.0")
			.description("API 명세서")
			.build();
	}
}
