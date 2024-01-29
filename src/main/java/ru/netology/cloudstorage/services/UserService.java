package ru.netology.cloudstorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.exceptions.UnauthorizedExceptionError;

import ru.netology.cloudstorage.models.User;
import ru.netology.cloudstorage.repositories.UserRepository;

/**
 * Данный класс UserService является сервисом, который реализует интерфейс UserDetailsService из Spring Security.
 * Этот интерфейс определяет метод loadUserByUsername, который используется для аутентификации пользователя
 * на основе его имени пользователя (логина).
 * <p>
 * - @Service - аннотация, которая указывает, что этот класс является сервисом Spring и будет использоваться для
 * выполнения бизнес-логики приложения.
 * - implements UserDetailsService - указывает, что этот класс реализует интерфейс UserDetailsService,
 * который предоставляет метод для загрузки информации о пользователе.
 * <p>
 * - private final UserRepository userRepository; - поле, которое хранит ссылку на репозиторий пользователей.
 * - @Autowired - аннотация, которая указывает, что зависимость от репозитория пользователей должна быть
 * автоматически внедрена в конструктор.
 * - public UserService(UserRepository userRepository) { ... } - конструктор класса UserService,
 * который принимает репозиторий пользователей и сохраняет его ссылку в поле userRepository.
 * <p>
 * - @Override - аннотация, которая указывает, что метод loadUserByUsername переопределяет метод из интерфейса
 * UserDetailsService.
 * - User user = userRepository.findUserByLogin(login); - вызов метода findUserByLogin репозитория пользователей для
 * поиска пользователя по его имени пользователя.
 * - if (user == null){ throw new ErrorUnauthorized(); } - проверка, если пользователь не найден, то выбрасывается
 * исключение ErrorUnauthorized, которое обрабатывается в другом месте приложения.
 * - return user; - возвращается найденный пользователь. Объект пользователя должен реализовывать интерфейс UserDetails,
 * который предоставляет информацию о пользователе для аутентификации и авторизации в приложении.
 */

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            throw new UnauthorizedExceptionError();
        }
        return user;
    }
}