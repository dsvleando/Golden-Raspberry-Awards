package br.com.outsera.gra.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Golden Raspberry Awards API").description(
				"API RESTful para gerenciamento dos Golden Raspberry Awards - prêmios para os piores filmes do ano")
				.version("1.0.0")
				.contact(new Contact().name("Outsera").email("contato@outsera.com").url("https://www.outsera.com"))
				.license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")));
	}
}
