package ru.netology.cloudstorage.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.netology.cloudstorage.models.User;
import ru.netology.cloudstorage.repositories.UserRepository;
import ru.netology.cloudstorage.exceptions.UnauthorizedExceptionError;

/**
 * Данный класс является юнит-тестом для класса UserService. В нем используется фреймворк Mockito для создания
 * мок-объектов и настройки их поведения.
 * <p>
 * Аннотация @ExtendWith(MockitoExtension.class) указывает на использование расширения Mockito при запуске тестов.
 * <p>
 * Аннотация @Mock создает мок-объект UserRepository, который будет использоваться в тестируемом классе UserService.
 * <p>
 * Аннотация @InjectMocks создает экземпляр UserService и автоматически внедряет созданный мок-объект
 * UserRepository в UserService.
 * <p>
 * Метод setUp() с аннотацией @BeforeEach устанавливает поведение мок-объекта UserRepository. Он говорит,
 * что при вызове метода findUserByLogin() с аргументом AUTH_USERNAME_3, должен быть возвращен объект USER_3.
 * <p>
 * Метод loadUserByNameNotAuthorizedException() тестирует ситуацию, когда пользователь не авторизован.
 * Он использует метод assertThrows() для проверки, что при вызове метода loadUserByUsername() с аргументом
 * AUTH_USERNAME_4 будет выброшено исключение UnauthorizedExceptionError.
 * <p>
 * Метод loadUserByName() тестирует ситуацию, когда пользователь авторизован. Он использует метод assertEquals()
 * для проверки, что при вызове метода loadUserByUsername() с аргументом AUTH_USERNAME_3 будет возвращен объект USER_3.
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ServiceUserTest {

    public static final Long AUTH_USER_ID_3 = 103L;
    public static final String AUTH_USERNAME_3 = "Auth_Username3";
    public static final String AUTH_PASSWORD_3 = "Auth_Password3";
    public static final User USER_3 = new User(AUTH_USER_ID_3, AUTH_USERNAME_3, AUTH_PASSWORD_3, null);
    //-------------------------------------------------------------------------------------------------------
    public static final String AUTH_USERNAME_4 = "Auth_Username4";
    //-------------------------------------------------------------------------------------------------------

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRep;

    @BeforeEach
    void setUp() {
        Mockito.when(userRep.findUserByLogin(AUTH_USERNAME_3)).thenReturn(USER_3);
    }

    @Test
    void loadUserByNameNotAuthorizedException() {
        Assertions.assertThrows(UnauthorizedExceptionError.class,
                () -> userService.loadUserByUsername(AUTH_USERNAME_4));
    }

    @Test
    void loadUserByName() {
        Assertions.assertEquals(USER_3, userService.loadUserByUsername(AUTH_USERNAME_3));
    }
}
