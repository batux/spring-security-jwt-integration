package com.demo.app.security.jwt.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.app.security.jwt.manager.JwtTokenManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.ExpiredJwtException;

import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private JwtTokenManager tokenManager;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenManager tokenManager) {
		super(authenticationManager);
		this.tokenManager = tokenManager;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		
		try {
			
			String jwtToken = tokenManager.extractJwtFromRequest(request);
			if(jwtToken == null) {
				chain.doFilter(request, response);
	            return;
			}
			
			if(StringUtils.hasText(jwtToken) && tokenManager.validate(jwtToken)) {

				String principal = tokenManager.getUsernameFromToken(jwtToken);
				List<SimpleGrantedAuthority>  roles = tokenManager.getRolesFromToken(jwtToken);

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						principal, null, roles);

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				chain.doFilter(request, response);
				return;
			}
			else {
				if(isBasicAuthRequest(request)) {
					SecurityContextHolder.clearContext();
					chain.doFilter(request, response);
					return;
				}
				prepareInvalidAuthResponse(response);
				return;
			}
		} 
		catch (ExpiredJwtException | BadCredentialsException ex) {
			prepareInvalidAuthResponse(response);
			return;
		} 
		catch(Exception ex) {
			prepareInvalidAuthResponse(response);
			return;
		}
	}
	
	private boolean isBasicAuthRequest(HttpServletRequest request) {
		String data = request.getHeader(HttpHeaders.AUTHORIZATION);
		return (StringUtils.hasText(data) && data.startsWith("Basic "));
	}
	
	private void prepareInvalidAuthResponse(HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader("INVALID-AUTH", "Invalid authentication attempt!");
	}
}
