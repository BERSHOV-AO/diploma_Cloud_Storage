package ru.netology.cloudstorage.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Данный класс File является моделью файла в приложении. Он представляет сущность файла,
 * которая будет сохраняться в базе данных.
 * <p>
 * - @Entity - аннотация, которая указывает, что этот класс является сущностью JPA и будет сохраняться в базе данных.
 * - @Data - аннотация из библиотеки Lombok, которая генерирует геттеры, сеттеры, методы equals, hashCode и toString
 * для всех полей класса.
 * - @NoArgsConstructor и @AllArgsConstructor - аннотации из Lombok, которые генерируют конструкторы без аргументов
 * (пустой конструктор) и конструкторы со всеми аргументами соответственно.
 * - @Table(name = "files") - аннотация, которая указывает, что сущность будет сохраняться в таблице с именем "files"
 * в базе данных.
 * <p>
 * - @Id - аннотация, которая указывает, что это поле является первичным ключом в базе данных.
 * - @GeneratedValue(strategy = GenerationType.IDENTITY) - аннотация, которая указывает, что значение этого поля будет
 * генерироваться автоматически базой данных при вставке новой записи.
 * <p>
 * - @Column(name = "file_content") - аннотация, которая указывает, что это поле будет сохранено в столбце с именем
 * "file_content" в базе данных.
 * - @NotNull - аннотация, которая указывает, что это поле не может быть null в базе данных.
 * - private byte[] fileContent; - поле типа byte[], которое представляет содержимое файла.
 * <p>
 * - @ManyToOne - аннотация, которая указывает на отношение "многие к одному" между таблицей файлов и таблицей
 * пользователей. Много файлов могут принадлежать одному пользователю.
 * - @JoinColumn(name = "user_id", referencedColumnName = "id") - аннотация, которая указывает, что поле "user" в
 * таблице файлов является внешним ключом, который ссылается на поле "id" в таблице пользователей.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename", unique = true)
    @jakarta.validation.constraints.NotNull
    private String filename;

    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    @Column(name = "size")
    @jakarta.validation.constraints.NotNull
    private Long size;

    @Column(name = "file_content")
    @NotNull
    private byte[] fileContent;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public File(String filename, LocalDateTime editedAt, long size, byte[] fileContent, User user) {
        this.filename = filename;
        this.editedAt = editedAt;
        this.size = size;
        this.fileContent = fileContent;
        this.user = user;
    }
}
