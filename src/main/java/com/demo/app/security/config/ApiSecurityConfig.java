package com.demo.app.security.config;

import com.demo.app.security.entrypoint.fail.AuthenticationFailureEntryPoint;
import com.demo.app.security.jwt.filter.JwtAuthorizationFilter;
import com.demo.app.security.jwt.manager.JwtTokenManager;
import com.demo.app.security.provider.UserAuthenticationProvider;
import com.demo.app.security.service.userdetails.AuthenticationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
	    securedEnabled = true,
	    jsr250Enabled = true,
	    prePostEnabled = true)
@Order(1)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthenticationFailureEntryPoint failureEntryPoint;
	
	@Autowired
	private JwtTokenManager tokenManager;

	@Autowired
	private AuthenticationUserDetailsService authenticationUserDetailsService;

	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http = http.cors().and().csrf().disable();
		
		http = http
	            .sessionManagement()
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and();
		
		http = http
	            .exceptionHandling()
	            .authenticationEntryPoint(failureEntryPoint)
	            .and();
		
		http.httpBasic().authenticationEntryPoint(failureEntryPoint);
		
		http.authorizeRequests()
				.antMatchers("/v2/api-docs", "/api-docs", "/swagger-ui/**", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
				.antMatchers(HttpMethod.POST, "/rest/api/v1/login").permitAll()
				.antMatchers(HttpMethod.POST, "/rest/api/v1/user").permitAll()
				.anyRequest().authenticated()
			.and()
			.addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenManager));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {

		UserAuthenticationProvider authenticationProvider = new UserAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		authenticationProvider.setUserDetailsService(authenticationUserDetailsService);
		auth.authenticationProvider(authenticationProvider);
	}

	@Bean
	public CorsFilter corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
