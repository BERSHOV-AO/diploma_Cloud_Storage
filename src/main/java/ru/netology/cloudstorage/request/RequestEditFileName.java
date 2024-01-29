package ru.netology.cloudstorage.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

/**
 * Данный класс RequestEditFileName является POJO (Plain Old Java Object) классом, который используется для передачи
 * данных в запросах.
 * <p>
 * Класс имеет одно приватное поле filename, а также конструктор и геттеры/сеттеры для этого поля.
 * <p>
 * Аннотация @Data от проекта Lombok автоматически генерирует геттеры, сеттеры, методы equals(),
 * hashCode() и toString() для всех полей класса.
 * <p>
 * Аннотация @JsonCreator указывает Jackson (библиотека для работы с JSON в Java),
 * что конструктор класса должен использоваться для создания объекта из JSON-строки.
 * В данном случае, конструктор принимает один аргумент filename, который будет инициализировать поле filename объекта.
 */
@Data
public class RequestEditFileName {

    private String filename;

    @JsonCreator
    public RequestEditFileName(String filename) {
        this.filename = filename;
    }
}