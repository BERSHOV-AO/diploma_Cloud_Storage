package ru.netology.cloudstorage.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.cloudstorage.models.User;
import org.junit.jupiter.api.Assertions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс теста RepAuthTest предназначен для проверки функциональности класса AuthRepository, который отвечает за
 * хранение и управление аутентификационными данными пользователей.
 * <p>
 * Перед каждым тестом выполняется метод authUp(), который создает экземпляр класса AuthRepository и сохраняет в
 * нем аутентификационные данные пользователя с токеном AUTH_TOKEN_1 и объектом USER_1. Также очищается и заполняется
 * tokensUser, который представляет собой мапу, где ключом является токен, а значением - объект пользователя.
 * <p>
 * Тест getUserByAuthToken() проверяет, что метод getAuthenticationUserByToken() возвращает ожидаемого пользователя
 * по заданному токену. С помощью метода Assertions.assertEquals() сравниваются значения, полученные из tokensUser и
 * из authRep.getAuthenticationUserByToken(AUTH_TOKEN_1).
 * <p>
 * Тест deleteAuthUserByToken() проверяет, что метод deleteAuthenticationUserByToken() удаляет пользователя из
 * репозитория по заданному токену. Сначала получаем пользователя до удаления с помощью метода
 * getAuthenticationUserByToken() и убеждаемся, что он не равен null. Затем вызываем метод
 * deleteAuthenticationUserByToken() для удаления пользователя. После этого снова вызываем
 * getAuthenticationUserByToken() и убеждаемся, что получаем null, что означает успешное удаление пользователя.
 * <p>
 * Тест setAuthTokenAndUser() проверяет, что метод saveAuthenticationUser() сохраняет пользователя в
 * репозитории по заданному токену. Сначала получаем пользователя до сохранения с помощью метода
 * getAuthenticationUserByToken() и убеждаемся, что он равен null. Затем вызываем метод saveAuthenticationUser()
 * для сохранения пользователя. После этого снова вызываем getAuthenticationUserByToken() и убеждаемся,
 * что получаем ожидаемого пользователя.
 * <p>
 * Все тесты используют класс Assertions из библиотеки JUnit, чтобы проверить ожидаемые значения.
 * Если ожидаемые значения совпадают с фактическими, то тест считается успешным, в противном случае - проваленным.
 */

public class RepAuthTest {
    public static final String AUTH_TOKEN_1 = "Auth_Token1";
    public static final Long AUTH_USER_ID_1 = 101L;
    public static final String AUTH_USERNAME_1 = "Auth_Username1";
    public static final String AUTH_PASSWORD_1 = "Auth_Password1";
    public static final User USER_1 = new User(AUTH_USER_ID_1, AUTH_USERNAME_1, AUTH_PASSWORD_1, null);

    //-------------------------------------------------------------------------------------------------------
    public static final String AUTH_TOKEN_2 = "Auth_Token2";
    public static final Long AUTH_USER_ID_2 = 102L;
    public static final String AUTH_USERNAME_2 = "Auth_Username2";
    public static final String AUTH_PASSWORD_2 = "Auth_Password2";
    public static final User USER_2 = new User(AUTH_USER_ID_2, AUTH_USERNAME_2, AUTH_PASSWORD_2, null);
    //-------------------------------------------------------------------------------------------------------

    private AuthRepository authRep;
    private final Map<String, User> tokensUser = new ConcurrentHashMap<>();

    @BeforeEach
    void authUp() {
        authRep = new AuthRepository();
        authRep.saveAuthenticationUser(AUTH_TOKEN_1, USER_1);
        tokensUser.clear();
        tokensUser.put(AUTH_TOKEN_1, USER_1);
    }

    @Test
    void getUserByAuthToken() {
        Assertions.assertEquals(tokensUser.get(AUTH_TOKEN_1), authRep.getAuthenticationUserByToken(AUTH_TOKEN_1));
    }

    @Test
    void deleteAuthUserByToken() {
        User userBeforeDel = authRep.getAuthenticationUserByToken(AUTH_TOKEN_1);
        Assertions.assertNotNull(userBeforeDel);
        authRep.deleteAuthenticationUserByToken(AUTH_TOKEN_1);
        User userAfterDel = authRep.getAuthenticationUserByToken(AUTH_TOKEN_1);
        Assertions.assertNull(userAfterDel);
    }

    @Test
    void setAuthTokenAndUser() {
        User userBeforeSet = authRep.getAuthenticationUserByToken(AUTH_TOKEN_2);
        Assertions.assertNull(userBeforeSet);
        authRep.saveAuthenticationUser(AUTH_TOKEN_2, USER_2);
        User userAfterSet = authRep.getAuthenticationUserByToken(AUTH_TOKEN_2);
        Assertions.assertEquals(USER_2, userAfterSet);
    }
}
