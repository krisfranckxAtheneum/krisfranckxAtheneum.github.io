package be.atheneumboom.bibliotheek;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.Magazine;
import be.atheneumboom.bibliotheek.model.Taal;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.service.BibItemService;
import be.atheneumboom.bibliotheek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.beans.Encoder;
import java.util.Arrays;


@SpringBootApplication
public class BibliotheekApplication extends SpringBootServletInitializer {

	public static void main(String[] args){

		SpringApplication.run(BibliotheekApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder){
		return springApplicationBuilder.sources(BibliotheekApplication.class);
	}


	/*@Bean
	public CorsFilter corsFilter(){
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:4200"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
		return new org.springframework.web.filter.CorsFilter(urlBasedCorsConfigurationSource);

	}*/



}



