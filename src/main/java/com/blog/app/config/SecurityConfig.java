package com.blog.app.config;

import com.blog.app.filter.JWTTokenGeneratorFilter;
import com.blog.app.filter.JWTTokenValidatorFilter;
import com.blog.app.filter.JWTValidatorExceptionsHandlerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

	private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
			"/api/test/**", "/authenticate", "/error", "/register", "/author", "/authenticate"};

	private final Environment env;

	public SecurityConfig(Environment env) {
		this.env = env;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {

		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

		http.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable()
//						.csrfTokenRequestHandler(requestHandler)
//						.ignoringRequestMatchers("/register", "/authenticate")
//						.csrfTokenRepository(new CookieCsrfTokenRepository())
				)

				.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new JWTTokenValidatorFilter(userDetailsService), BasicAuthenticationFilter.class)
				.addFilterBefore(new JWTValidatorExceptionsHandlerFilter(), JWTTokenValidatorFilter.class)

				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(WHITE_LIST_URL).permitAll()
						.requestMatchers("/user").hasAuthority("ADMIN")
						.requestMatchers("/users/delete/**").hasAuthority("ADMIN")
						.anyRequest().authenticated());

		http.formLogin(Customizer.withDefaults());
		http.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
	                                                   PasswordEncoder passwordEncoder) {

		BlogAppUsernamePwdAuthProvider authProvider =
				new BlogAppUsernamePwdAuthProvider(userDetailsService, passwordEncoder);

		ProviderManager providerManager = new ProviderManager(authProvider);
		providerManager.setEraseCredentialsAfterAuthentication(false);
		return providerManager;
	}


/*	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}*/
}
