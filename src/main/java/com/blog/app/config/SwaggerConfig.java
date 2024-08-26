package com.blog.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
//				.info(new Info().title("Blog App"))
//				.addSecurityItem(new SecurityRequirement().addList("BlogAppSecurityScheme"))
//				.components(new Components().addSecuritySchemes("BlogAppSecurityScheme",
//						new SecurityScheme().name("BlogAppSecurityScheme")
//								.type(SecurityScheme.Type.APIKEY)
//								.scheme(String.valueOf(In.HEADER)))
//								.name("JWT"));

				.info(new Info().title("Blog App"))
				.addSecurityItem(new SecurityRequirement().addList("JWT"))
				.components(new io.swagger.v3.oas.models.Components()
						.addSecuritySchemes("JWT", new SecurityScheme()
								.name("Authorization")
								.type(SecurityScheme.Type.APIKEY) // Use APIKEY instead of HTTP
								.in(SecurityScheme.In.HEADER)
								.name("Authorization")
						)
				);
	}

}
