package ru.netology.cloudstorage.repositories;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.netology.cloudstorage.models.File;
import ru.netology.cloudstorage.models.User;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Класс тестов RepFileTest использует фреймворк Spring для проведения интеграционного тестирования репозитория
 * FileRepository.
 * <p>
 * Аннотация @RunWith(SpringRunner.class) указывает, что тесты будут запускаться с помощью SpringRunner,
 * который предоставляет функциональность Spring для тестирования.
 * <p>
 * Аннотация @DataJpaTest указывает, что тесты будут использовать встроенную базу данных и автоматически
 * настраивать Spring Data JPA.
 * <p>
 * Аннотация @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) указывает,
 * чтобы не заменять настройки базы данных, определенные в файле application.properties.
 *
 * @Autowired используется для автоматической инъекции зависимостей. В данном случае, FileRepository
 * и UserRepository будут автоматически созданы и инициализированы Spring'ом.
 * @BeforeEach метод setUp() выполняется перед каждым тестом и инициализирует данные для тестов.
 * Создается объект User и сохраняется в UserRepository, затем создается объект File, который ссылается
 * на сохраненного пользователя, и сохраняется в FileRepository.
 * @Test метод findAllByUser() проверяет, что метод findAllByUser() возвращает список файлов, принадлежащих
 * определенному пользователю. Сравнивается ожидаемый результат (список с одним элементом fileRepSave)
 * с фактическим результатом вызова метода.
 * @Test метод findByUserAndFilename() проверяет, что метод findByUserAndFilename() возвращает файл с
 * определенным пользователем и именем файла. Сравнивается ожидаемый результат (fileRepSave) с фактическим
 * результатом вызова метода.
 * @Test метод deleteUserAndFilename() проверяет, что метод deleteByUserAndFilename() удаляет файл с определенным
 * пользователем и именем файла. Сначала проверяется, что файл существует, затем вызывается метод
 * deleteByUserAndFilename() и проверяется, что количество удаленных строк равно 1.
 * <p>
 * Таким образом, класс тестов RepFileTest проверяет работу методов репозитория FileRepository.
 */

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepFileTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;
    private User userRepSave;
    private File fileRepSave;

    @BeforeEach
    void setUp() {
        User user = new User(RandomUtils.nextLong(), "login1", "password1", null);
        userRepSave = userRepository.save(user);

        File file = new File(RandomUtils.nextLong(), "file_name", LocalDateTime.now(),
                RandomUtils.nextLong(), "".getBytes(), userRepSave);
        fileRepSave = fileRepository.save(file);
    }

    @Test
    void findAllByUser() {
        assertEquals(List.of(fileRepSave), fileRepository.findAllByUser(userRepSave));
    }

    @Test
    void findByUserAndFilename() {
        assertEquals(fileRepSave, fileRepository.findByUserAndFilename(userRepSave, fileRepSave.getFilename()));
    }

    @Test
    void deleteUserAndFilename() {
        File beforeDel = fileRepository.findByUserAndFilename(userRepSave, fileRepSave.getFilename());
        assertNotNull(beforeDel);
        int delRows = fileRepository.deleteByUserAndFilename(userRepSave, fileRepSave.getFilename());
        Assert.assertEquals(delRows, 1);
    }
}
