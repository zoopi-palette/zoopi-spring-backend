package com.zoopi.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.zoopi.config.security.oauth2.CustomOAuth2UserService;
import com.zoopi.config.security.oauth2.OAuth2SuccessHandler;
import com.zoopi.filter.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtRequestFilter requestFilter;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final CustomOAuth2UserService oAuth2UserService;

	private static final String[] AUTH_WHITELIST = {"/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
		"/configuration/security", "/swagger-ui.html/**", "/webjars/**", "/swagger/**"};

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(AUTH_WHITELIST);
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.logout().disable()
			.cors().configurationSource(configurationSource())
			.and()

			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler)
			.authenticationEntryPoint(authenticationEntryPoint)
			.and()

			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()

			.authorizeRequests()
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
			.antMatchers("/auth/**", "/oauth2/**").permitAll()
			.anyRequest().authenticated()
			.and()

			.oauth2Login()
			.redirectionEndpoint().baseUri("/oauth2/callback/*")
			.and()

			.successHandler(oAuth2SuccessHandler)
			.userInfoEndpoint().userService(oAuth2UserService)
		;

		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public CorsConfigurationSource configurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}
