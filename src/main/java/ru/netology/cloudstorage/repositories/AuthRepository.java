package ru.netology.cloudstorage.repositories;

import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.models.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *Данный класс AuthRepository является репозиторием для аутентификации пользователей.
 * Он предоставляет методы для сохранения, удаления и получения информации об аутентифицированных пользователях.
 *
 * - Метод saveUserAuthentication сохраняет аутентификационный токен и соответствующего пользователя
 * в usersAuthenticationMap.
 * - Метод removeAuthenticationUserByToken удаляет аутентифицированного пользователя по заданному аутентификационному
 * токену из usersAuthenticationMap.
 * - Метод getAuthenticationUserByToken возвращает аутентифицированного пользователя по заданному аутентификационному
 * токену из usersAuthenticationMap.
 *
 * Таким образом, данный класс предоставляет удобный способ для управления аутентификацией пользователей, хранения
 * и получения информации о них.
 */

@Repository
public class AuthRepository {
    private static final Map<String, User> usersAuthenticationMap = new ConcurrentHashMap<>();

    public void saveUserAuthentication(String authenticationToken, User user) {
        usersAuthenticationMap.put(authenticationToken, user);
    }

    public void removeAuthenticationUserByToken(String authenticationToken) {
        usersAuthenticationMap.remove(authenticationToken);
    }

    public User getAuthenticationUserByToken(String authenticationToken) {
        return usersAuthenticationMap.get(authenticationToken);
    }
}
