package ru.netology.cloudstorage.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloudstorage.services.UserService;

import java.io.IOException;

/**
 * Класс JWTFilter является фильтром, который применяется для каждого запроса и выполняет следующие действия:
 * <p>
 * 1. Получает заголовок запроса с именем "auth-token" (request.getHeader("auth-token")).
 * 2. Проверяет, начинается ли значение заголовка с "Bearer " (requestTokenHeader.startsWith("Bearer ")).
 * 3. Если значение заголовка начинается с "Bearer ", то извлекает JWT токен из заголовка, удаляя префикс "Bearer
 * " (jwtToken = requestTokenHeader.substring(7)).
 * 4. Пытается извлечь имя пользователя из токена, используя метод getUsernameFromToken класса JWTUtils.
 * 5. Если удалось извлечь имя пользователя, то проверяет, что аутентификация не была уже установлена
 * (SecurityContextHolder.getContext().getAuthentication() == null).
 * 6. Загружает информацию о пользователе (UserDetails) из базы данных по имени пользователя, используя сервис
 * userService.loadUserByUsername.
 * 7. Проверяет, является ли токен действительным для указанного пользователя, используя метод isValidateToken
 * класса JWTUtils.
 * 8. Если токен действителен, то создает объект UsernamePasswordAuthenticationToken, который представляет
 * аутентификацию пользователя, и устанавливает его в контекст безопасности
 * (SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken)).
 * 9. Продолжает выполнение цепочки фильтров (filterChain.doFilter(request, response)).
 * <p>
 * Таким образом, класс JWTFilter выполняет проверку и аутентификацию JWT токена для каждого запроса,
 * и если токен действителен, устанавливает аутентификацию пользователя в контекст безопасности.
 */

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final UserService userService;

    public JWTFilter(JWTUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("auth-token");
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtils.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtUtils.isValidateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}




