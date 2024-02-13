package ru.netology.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudstorage.request.RequestAuth;

import jakarta.validation.Valid;
import ru.netology.cloudstorage.response.JwtTokenResponse;
import ru.netology.cloudstorage.services.AuthenticationService;

/**
 * Этот класс является контроллером, который обрабатывает HTTP-запросы для аутентификации и авторизации пользователей.
 * <p>
 * - В конструкторе класса AuthController происходит внедрение зависимости сервиса AuthService, который отвечает
 * за логику аутентификации и авторизации.
 * <p>
 * - Метод login обрабатывает POST-запрос на пути "/login". Он принимает валидированный JSON-объект RequestAuth в
 * теле запроса, содержащий данные для аутентификации пользователя. Затем метод вызывает метод login сервиса
 * authService, передавая ему объект requestAuth. Результатом вызова метода является объект ResponseJWT,
 * который содержит JWT-токен для аутентифицированного пользователя. Метод возвращает этот объект в теле
 * ответа с кодом статуса 200 (OK).
 * <p>
 * - Метод logout обрабатывает POST-запрос на пути "/logout". Он принимает заголовок "auth-token",
 * содержащий JWT-токен пользователя. Затем метод вызывает метод logout сервиса authService, передавая ему токен.
 * Метод не возвращает никаких данных в теле ответа, только сообщение "Success logout" с кодом статуса 200 (OK).
 * <p>
 * В целом, класс AuthController служит для обработки запросов аутентификации и авторизации пользователей,
 * вызывая соответствующие методы сервиса AuthService и возвращая результаты обратно в виде JSON-объектов с
 * соответствующими кодами статуса HTTP.
 * <p>
 * ResponseEntity<?> указывает на то, что тип возвращаемого значения может быть любым. Он является wildcard-типом
 * (тип с подстановочным знаком) и используется для обозначения неизвестного или неопределенного типа.
 * В данном случае, ResponseEntity<?> означает, что метод login может возвращать объект любого типа. Это может быть
 * полезно, когда точный тип возвращаемого значения неизвестен или может измениться в зависимости от
 * различных условий или сценариев.
 * Использование wildcard-типа позволяет гибко работать с различными типами данных и обрабатывать их в общем контексте,
 * без необходимости указывать конкретный тип.
 */
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody RequestAuth requestAuth) {
        JwtTokenResponse token = authenticationService.login(requestAuth);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken) {
        authenticationService.logout(authToken);
        return new ResponseEntity<>("Success logout", HttpStatus.OK);
    }

}