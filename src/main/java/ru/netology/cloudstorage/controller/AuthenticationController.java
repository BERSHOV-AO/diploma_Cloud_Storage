package ru.netology.cloudstorage.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudstorage.dto.LoginDTO;
import ru.netology.cloudstorage.handl_except.InvalidCredentialsException;
import ru.netology.cloudstorage.security.JWTUtils;


import java.util.Collections;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    public AuthenticationController(JWTUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Map<String, Object> loginAuthHandler(@RequestBody LoginDTO bodyLogin) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(bodyLogin.getLogin(), bodyLogin.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            String authToken = jwtUtils.generateToken(bodyLogin.getLogin());

            return Collections.singletonMap("auth-token", authToken);
        } catch (AuthenticationException autExcept) {
            throw new InvalidCredentialsException("Failed credential verification" + autExcept);
        }
    }
}
