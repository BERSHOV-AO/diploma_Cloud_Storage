package ru.netology.cloudstorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.exceptions.BadCredentialsExceptionError;
import ru.netology.cloudstorage.models.User;
import ru.netology.cloudstorage.repositories.AuthRepository;
import ru.netology.cloudstorage.repositories.UserRepository;
import ru.netology.cloudstorage.request.RequestAuth;
import ru.netology.cloudstorage.response.JwtTokenResponse;
import ru.netology.cloudstorage.security.JWTUtils;


/**
 * Данный класс AuthService отвечает за логику аутентификации и авторизации пользователей.
 * <p>
 * - В конструкторе класса AuthService происходит внедрение зависимостей:
 * - UserRepository - репозиторий для работы с данными пользователей;
 * - AuthRepository - репозиторий для работы с данными аутентификации пользователей;
 * - JWTUtils - утилита для работы с JWT-токенами;
 * - AuthenticationManager - менеджер аутентификации, который будет использоваться для проверки учетных данных
 * пользователя.
 * <p>
 * - Метод login принимает объект RequestAuth, который содержит данные для аутентификации пользователя. Внутри метода
 * происходит попытка аутентификации пользователя с помощью authenticationManager.authenticate. Если аутентификация не
 * прошла успешно, то выбрасывается исключение ErrorBadCredentials. Если аутентификация прошла успешно, то происходит
 * поиск пользователя в репозитории userRepository по логину из requestAuth. Затем генерируется JWT-токен с помощью
 * jwtUtils.generateToken и сохраняется в репозитории authRepository вместе с информацией о пользователе. Наконец,
 * метод возвращает объект ResponseJWT, содержащий сгенерированный токен.
 * <p>
 * - Метод logout принимает JWT-токен пользователя в виде строки authToken. Сначала из строки authToken извлекается
 * сам токен путем удаления префикса "Bearer ". Затем происходит удаление информации об аутентифицированном пользователе
 * из репозитория authRepository с использованием метода deleteAuthenticationUserByToken.
 * <p>
 * В целом, класс AuthService отвечает за аутентификацию и авторизацию пользователей. Он использует репозитории для
 * работы с данными пользователей и аутентификации, утилиту для работы с JWT-токенами и менеджер аутентификации.
 * Методы login и logout выполняют соответствующие операции и возвращают результаты обратно в контроллер AuthController.
 * <p>
 * В данном случае, строка "authToken" представляет собой JWT-токен, который начинается с префикса "Bearer ".
 * Метод "substring(7)" используется для удаления этого префикса из токена. Число 7 указывает на количество символов,
 * которые нужно удалить, чтобы получить сам токен без префикса.
 * <p>
 * Пример:
 * Если значение "authToken" равно "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 * то после вызова "authToken.substring(7)" будет получено значение "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...".
 * Таким образом, префикс "Bearer " будет удален из токена.
 */
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final AuthRepository authRepository;

    private final JWTUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, AuthRepository authRepository, JWTUtils jwtUtils,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public JwtTokenResponse login(RequestAuth requestAuth) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestAuth.getLogin(),
                    requestAuth.getPassword()));
        } catch (BadCredentialsExceptionError e) {
            throw new BadCredentialsExceptionError();
        }
        User user = userRepository.findUserByLogin(requestAuth.getLogin());
        String token = jwtUtils.generateToken(user);
        authRepository.saveUserAuthentication(token, user);
        return new JwtTokenResponse(token);
    }

    public void logout(String authToken) {
        String jwt = authToken.substring(7);
        authRepository.removeAuthenticationUserByToken(jwt);
    }
}
