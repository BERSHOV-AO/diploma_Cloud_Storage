package ru.netology.cloudstorage.response;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Данный класс ResponseFile является POJO (Plain Old Java Object) классом, который используется для передачи
 * данных в ответах.
 * <p>
 * Класс имеет два приватных поля filename и size, а также конструктор и геттеры/сеттеры для этих полей.
 * <p>
 * Аннотация @Data от проекта Lombok автоматически генерирует геттеры, сеттеры, методы equals(),
 * hashCode() и toString() для всех полей класса.
 * <p>
 * Аннотация @AllArgsConstructor от проекта Lombok генерирует конструктор, который принимает все поля класса
 * в качестве аргументов. В данном случае, конструктор принимает два аргумента filename и size, которые будут
 * инициализировать соответствующие поля объекта.
 */
@Data
@AllArgsConstructor
public class ResponseFile {
    private String filename;
    private long size;
}