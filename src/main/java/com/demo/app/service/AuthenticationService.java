package com.demo.app.service;

import com.demo.app.model.AuthenticationResponse;
import com.demo.app.model.UserContext;
import com.demo.app.security.jwt.manager.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenManager tokenManager;


    public AuthenticationResponse authenticate(String authorization) {

        if(!StringUtils.hasText(authorization)) {
            throw new RuntimeException("INVALID AUTH PAYLOAD");
        }

        String[] httpBasicAuthPayload = parseHttpBasicPayload(authorization);
        String email = httpBasicAuthPayload[0];
        String password = httpBasicAuthPayload[1];

        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );

        User user = (User) authenticate.getPrincipal();
        if(user == null) {
            throw new RuntimeException("USER NOT FOUND");
        }

        UserContext userContext = userService.load(email);
        String jwtToken = tokenManager.generateToken(user, userContext.getId());
        return new AuthenticationResponse(userContext.getId(), jwtToken);
    }

    private String[] parseHttpBasicPayload(String authorization) {

        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {

            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            return credentials.split(":", 2);
        }
        return null;
    }
}
