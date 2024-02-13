package ru.netology.cloudstorage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DiplomaCloudStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomaCloudStorageApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PasswordEncoder encoder) {
        return args -> {
            String password = "bershov2";
            String encryptedPassword = encoder.encode(password);
            System.out.println("Encrypted password: " + encryptedPassword);

        };
    }

}
