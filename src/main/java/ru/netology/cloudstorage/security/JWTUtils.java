package ru.netology.cloudstorage.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Класс JWTUtils предоставляет набор методов для работы с JSON Web Token (JWT). JWT - это стандарт для создания токенов
 * аутентификации, которые могут передаваться между клиентом и сервером для проверки подлинности и авторизации.
 *
 * Класс содержит следующие методы:
 *
 * 1. generateToken(UserDetails userDetails) - генерирует JWT токен на основе информации о пользователе (UserDetails).
 * Метод создает пустой набор утверждений (claims), устанавливает в него имя пользователя (setSubject), устанавливает
 * время выдачи токена (setIssuedAt), устанавливает время истечения токена (setExpiration) и подписывает токен с
 * использованием секретного ключа (signWith). Возвращает сгенерированный токен.
 *
 * 2. isValidateToken(String token, UserDetails userDetails) - проверяет, является ли переданный токен действительным
 * для указанного пользователя. Метод извлекает имя пользователя из токена (getUsernameFromToken) и проверяет, совпадает
 * ли оно с именем пользователя из UserDetails. Также метод проверяет, не истекло ли время действия токена
 * (isTokenExpired). Возвращает true, если токен действителен для указанного пользователя, иначе возвращает false.
 *
 * 3. isTokenExpired(String token) - проверяет, истекло ли время действия токена. Метод извлекает время истечения
 * токена из утверждений (getClaimFromToken) и сравнивает его с текущим временем. Возвращает true, если время истекло,
 * иначе возвращает false.
 *
 * 4. getUsernameFromToken(String token) - извлекает имя пользователя из токена. Метод извлекает утверждение "subject"
 * из токена (getClaimFromToken) и возвращает его значение.
 *
 * 5. getClaimFromToken(String token, Function<Claims, T> claimsResolver) - извлекает указанное утверждение из токена.
 * Метод разбирает токен с использованием секретного ключа (jwtSecret), получает тело токена (getBody) и вызывает
 * переданную функцию (claimsResolver) для извлечения указанного утверждения. Возвращает значение утверждения.
 *
 * Все методы используются для работы с JWT токенами, включая генерацию, проверку и извлечение информации из токена.
 */
@Component
public class JWTUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;


    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 64 * 24))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }


    public Boolean isValidateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}
