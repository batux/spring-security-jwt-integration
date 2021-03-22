package com.demo.app.controller;

import com.demo.app.model.AuthenticationResponse;
import com.demo.app.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/rest/api/v1")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthenticationResponse> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {

            AuthenticationResponse response = authenticationService.authenticate(authorization);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, response.getToken())
                    .body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
