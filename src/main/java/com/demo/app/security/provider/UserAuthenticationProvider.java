package com.demo.app.security.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
		
		return new UsernamePasswordAuthenticationToken(principal, user.getPassword(), user.getAuthorities());
	}
}
