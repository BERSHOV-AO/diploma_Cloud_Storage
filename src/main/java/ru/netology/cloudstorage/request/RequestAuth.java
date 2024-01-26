package ru.netology.cloudstorage.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Данный класс представляет модель запроса аутентификации. Он содержит два поля: "login" и "password".
 * <p>
 * Аннотация @Data генерирует для данного класса все необходимые методы геттеров, сеттеров, методы equals(),
 * hashCode() и toString(). Это позволяет упростить работу с объектами этого класса.
 * <p>
 * Аннотация @AllArgsConstructor создает конструктор, принимающий все поля класса в качестве аргументов.
 * Это позволяет создать объект класса RequestAuth и инициализировать его поля значениями, переданными в конструкторе.
 * <p>
 * Аннотация @NotBlank применена к полям "login" и "password" класса RequestAuth. Она проверяет,
 * что значения этих полей не являются пустыми или состоят только из пробелов. Если значение поля не удовлетворяет
 * этому условию, то будет выброшено исключение.
 */

@Data
@AllArgsConstructor
public class RequestAuth {

    @NotBlank
    private String login;
    @NotBlank
    private String password;
}
