package ru.netology.cloudstorage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudstorage.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
}
