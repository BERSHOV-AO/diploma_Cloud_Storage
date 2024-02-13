package ru.netology.cloudstorage.models;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;


/**
 * Данный класс User является моделью пользователя в приложении. Он реализует интерфейс UserDetails из Spring Security,
 * который предоставляет информацию о пользователе для аутентификации и авторизации.
 * <p>
 * - @Entity - аннотация, которая указывает, что этот класс является сущностью JPA и будет сохраняться в базе данных.
 * - @Getter и @Setter - аннотации из библиотеки Lombok, которые генерируют геттеры и сеттеры для всех полей класса.
 * - @NoArgsConstructor и @AllArgsConstructor - аннотации из Lombok, которые генерируют конструкторы без аргументов
 * (пустой конструктор) и конструкторы со всеми аргументами соответственно.
 * - @Table(name = "users") - аннотация, которая указывает, что сущность будет сохраняться в таблице с именем "users"
 * в базе данных.
 *
 * @Id - аннотация, которая указывает, что это поле является первичным ключом в базе данных.
 * - @GeneratedValue(strategy = GenerationType.IDENTITY) - аннотация, которая указывает, что значение этого поля будет
 * генерироваться автоматически базой данных при вставке новой записи.
 * <p>
 * - @Column(name = "login", unique = true) - аннотация, которая указывает, что это поле будет сохранено в столбце
 * с именем "login" в базе данных, и что значение этого поля должно быть уникальным.
 * - @NotNull - аннотация, которая указывает, что это поле не может быть null в базе данных.
 * <p>
 * - @OneToMany - аннотация, которая указывает на отношение "один ко многим" между таблицей пользователей и
 * таблицей файлов. Один пользователь может иметь много файлов.
 * - mappedBy = "user" - указывает на поле "user" в классе File, которое является владельцем отношения.
 * - cascade = CascadeType.ALL - указывает, что все операции (вставка, обновление, удаление) должны быть
 * применены ко всем связанным сущностям (файлам) при выполнении операции с пользователем.
 * - fetch = FetchType.EAGER - указывает, что все связанные сущности (файлы) должны быть загружены
 * немедленно при загрузке пользователя.
 */


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true)
    @jakarta.validation.constraints.NotNull
    private String login;

    @Column(name = "password")
    @NotNull
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<File> userFiles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public String getUserPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

