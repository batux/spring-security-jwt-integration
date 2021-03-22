package com.demo.app.controller;

import com.demo.app.model.UserContext;
import com.demo.app.security.annotation.IdGuard;
import com.demo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/rest/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody UserContext userContext) {

        return ResponseEntity.ok().body(userService.save(userContext));
    }

    @IdGuard(parameterIndex = 0)
    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserContext> load(@PathVariable("userId") Integer userId) {

        return ResponseEntity.ok().body(userService.load(userId));
    }
}
