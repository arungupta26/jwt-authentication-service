package com.jwt.authentication.configuration;

import com.jwt.authentication.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterConfiguration {

	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityFilterConfiguration(AuthenticationEntryPoint authenticationEntryPoint,
			JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		//Disable CORS
		httpSecurity.cors(AbstractHttpConfigurer::disable);

		//Disable CSRF
		httpSecurity.csrf(AbstractHttpConfigurer::disable);

		// filter Http request filter
		httpSecurity.authorizeHttpRequests(
				authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers(
								"/api/auth/login/**")
						.permitAll()
						.requestMatchers("/api/auth/signup/**")
						.permitAll()
						.anyRequest()
						.authenticated());

		//authentication entry point
		httpSecurity.exceptionHandling(
				httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
						authenticationEntryPoint));

		//set session policy to STATELESS
		httpSecurity.sessionManagement(
				httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
						SessionCreationPolicy.STATELESS));

		//add JWT authentication filer
		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();

	}

}
