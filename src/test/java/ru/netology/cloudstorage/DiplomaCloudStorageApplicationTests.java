package ru.netology.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Данный класс теста является интеграционным тестом для проверки работоспособности базы данных PostgreSQL.
 * Аннотация @Testcontainers указывает, что тест будет использовать контейнеры Docker для запуска
 * PostgreSQL базы данных.
 * Аннотация @SpringBootTest указывает, что тест будет запускаться в контексте Spring Boot.
 * Аннотация @ContextConfiguration с аргументом Initializer.class указывает,
 * что тест будет использовать специальный инициализатор Initializer для настройки окружения приложения.
 * В блоке с переменными задаются порт и настройки для контейнера PostgreSQL.
 * Затем создается контейнер dbContainer для запуска PostgreSQL базы данных.
 * Контейнер настроен на использование сети NETWORK, а также на использование порта DB_PORT и заданных
 * имени базы данных, имени пользователя и пароля.
 * В методе contLoadsPostgres() проверяется, что контейнер dbContainer успешно запущен. Для этого выводятся
 * в консоль адрес базы данных, имя базы данных и пароль, а также ID сети NETWORK и порт, на котором
 * запущена база данных. Затем с помощью Assertions.assertTrue() проверяется, что контейнер dbContainer работает.
 * <p>
 * Класс Initializer реализует интерфейс ApplicationContextInitializer<ConfigurableApplicationContext>
 * и используется для настройки окружения приложения перед его запуском. В данном случае он устанавливает
 * значения свойств spring.datasource.url, spring.datasource.username и spring.datasource.password
 * в соответствии с настройками контейнера dbContainer.
 * Таким образом, данный класс теста проверяет работоспособность базы данных PostgreSQL, используя контейнер Docker.
 */

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {DiplomaCloudStorageApplicationTests.Initializer.class})
public class DiplomaCloudStorageApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    private static final int DB_PORT = 5432;
    private static final String DB_NAME = "postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private final static Network NETWORK = Network.newNetwork();

    @Container
    public static PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres")
            .withNetwork(NETWORK)
            .withExposedPorts(DB_PORT)
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USERNAME)
            .withPassword(DB_PASSWORD);

    @Test
    void contLoadsPostgres() {
        var portDatabase = dbContainer.getMappedPort(DB_PORT);
        System.out.println(dbContainer.getJdbcUrl() + " " + dbContainer.getDatabaseName() + " " + dbContainer.getPassword());
        System.out.println("Network id: " + NETWORK.getId());
        System.out.println("database -> port: " + portDatabase);
        Assertions.assertTrue(dbContainer.isRunning());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + dbContainer.getJdbcUrl(),
                    "spring.datasource.username=" + dbContainer.getUsername(),
                    "spring.datasource.password=" + dbContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}