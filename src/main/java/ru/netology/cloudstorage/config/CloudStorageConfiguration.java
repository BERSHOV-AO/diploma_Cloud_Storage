package ru.netology.cloudstorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.netology.cloudstorage.security.JWTUtil;

@Configuration
public class CloudStorageConfiguration {

    @Bean
    public JWTUtil jwtUtil(UserDetailsService userDetailsService) {
        return new JWTUtil(userDetailsService);
    }
}
