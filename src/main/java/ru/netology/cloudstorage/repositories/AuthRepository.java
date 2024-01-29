package ru.netology.cloudstorage.repositories;

import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.models.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Данный класс AuthRepository является репозиторием для аутентификации пользователей.
 * Он предоставляет методы для сохранения, удаления и получения информации об аутентифицированных пользователях.
 * <p>
 * - Метод saveUserAuthentication сохраняет аутентификационный токен и соответствующего пользователя
 * в usersAuthenticationMap.
 * - Метод removeAuthenticationUserByToken удаляет аутентифицированного пользователя по заданному аутентификационному
 * токену из usersAuthenticationMap.
 * - Метод getAuthenticationUserByToken возвращает аутентифицированного пользователя по заданному аутентификационному
 * токену из usersAuthenticationMap.
 * <p>
 * Таким образом, данный класс предоставляет удобный способ для управления аутентификацией пользователей, хранения
 * и получения информации о них.
 */

@Repository
public class AuthRepository {

    private static final Map<String, User> mapAuthenticationUsers = new ConcurrentHashMap<>();

    public void saveAuthenticationUser(String authToken, User user) {
        mapAuthenticationUsers.put(authToken, user);
    }

    public void deleteAuthenticationUserByToken(String authToken) {
        mapAuthenticationUsers.remove(authToken);
    }

    public User getAuthenticationUserByToken(String authToken) {
        return mapAuthenticationUsers.get(authToken);
    }
}

