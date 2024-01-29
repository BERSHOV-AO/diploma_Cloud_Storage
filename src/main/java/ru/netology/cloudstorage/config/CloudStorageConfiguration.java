package ru.netology.cloudstorage.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.netology.cloudstorage.security.JWTFilter;
import ru.netology.cloudstorage.services.UserService;

import java.util.List;


/**
 * Аннотация @Configuration указывает, что данный класс является конфигурационным классом для Spring. Он определяет
 * настройки и конфигурации для приложения.
 * Аннотация @EnableWebSecurity включает поддержку безопасности веб-приложения. Она активирует класс
 * WebSecurityConfigurerAdapter, который позволяет настроить настройки безопасности для приложения.
 * Аннотация @EnableMethodSecurity включает поддержку безопасности методов. Она позволяет использовать аннотации
 * безопасности (@Secured, @PreAuthorize, @PostAuthorize) для аутентификации и авторизации методов в приложении.
 * Вместе эти три аннотации позволяют определить и настроить правила безопасности для приложения,
 * включая аутентификацию, авторизацию и защиту методов.
 * Данный класс CloudStorageFilesConfig является конфигурационным классом для Spring Security. Он определяет настройки
 * безопасности и авторизации для приложения.
 * <p>
 * В методе filterChain определяются основные настройки безопасности. Здесь отключается CSRF защита,
 * включается поддержка CORS (Cross-Origin Resource Sharing) для обработки запросов из разных источников,
 * устанавливается политика управления сессиями (без сохранения состояния) и задаются правила авторизации
 * для различных HTTP-запросов.
 * <p>
 * Метод authenticationProvider создает объект DaoAuthenticationProvider, который используется для аутентификации
 * пользователей. Он устанавливает сервис пользователя (userService) для получения информации о пользователях и
 * кодировщик паролей (passwordEncoder) для проверки пароля при аутентификации.
 * <p>
 * Метод authenticationManager создает объект AuthenticationManager, который используется для аутентификации
 * пользователей. Он получает объект AuthenticationConfiguration для получения настроек аутентификации.
 * <p>
 * Метод passwordEncoder создает объект BCryptPasswordEncoder, который используется для хеширования паролей
 * пользователей.
 * <p>
 * Метод corsConfigurationSource создает объект CorsConfigurationSource, который определяет настройки CORS для
 * приложения. Здесь задаются разрешенные источники (allowedOrigins), методы (allowedMethods) и
 * заголовки (allowedHeaders) для CORS.
 * <p>
 * Также в классе определены поля userService и jwtFilter, которые внедряются через конструктор.
 * userService используется для получения информации о пользователях, а jwtFilter - для обработки
 * JWT-токенов при аутентификации.
 * <p>
 * В целом, данный класс определяет настройки безопасности и авторизации для приложения,
 * включая аутентификацию пользователей, настройки CORS и обработку JWT-токенов.
 * <p>
 * Аннотация @Value используется для внедрения значений из внешних источников (например, файлы конфигурации)
 * в поля класса.
 * В данном коде, аннотация @Value("true") указывает, что поле credentials будет иметь значение true.
 * Это значение будет использоваться при настройке CORS для указания, должны ли быть передаваемые учетные
 * данные (например, куки, заголовки авторизации) включены в CORS-запросы или нет.
 * Аннотация @Value("http://localhost:8080") указывает, что поле origins будет иметь значение "http://localhost:8080".
 * Это значение будет использоваться при настройке CORS для указания разрешенных источников (доменов),
 * с которых разрешены CORS-запросы.
 * Аннотация @Value("*") указывает, что поля methods и headers будут иметь значение "*", что означает,
 * что все методы запросов и все заголовки разрешены со стороны клиента.
 * Таким образом, эти аннотации @Value позволяют внедрять значения из внешних источников в поля класса,
 * чтобы настроить безопасность и CORS в приложении.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CloudStorageConfiguration {
    private final UserService userService;
    private final JWTFilter jwtFilter;
    @Value("${cors.credentials}")
    private Boolean credentials;

    @Value("${cors.origins}")
    private String origins;

    @Value("${cors.methods}")
    private String methods;

    @Value("${cors.headers}")
    private String headers;

    @Autowired
    public CloudStorageConfiguration(UserService userService, JWTFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors().and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/login").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(credentials);
        configuration.setAllowedOrigins(List.of(origins));
        configuration.setAllowedMethods(List.of(methods));
        configuration.setAllowedHeaders(List.of(headers));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
