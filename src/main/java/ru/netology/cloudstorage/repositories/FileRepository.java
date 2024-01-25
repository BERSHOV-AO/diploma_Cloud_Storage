package ru.netology.cloudstorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.netology.cloudstorage.models.File;
import ru.netology.cloudstorage.models.User;

import java.util.List;

/**
 * Данный класс является репозиторием для работы с сущностями File в базе данных. Он расширяет интерфейс JpaRepository,
 * который предоставляет стандартные методы для работы с базой данных, такие как сохранение, удаление и поиск объектов.
 *
 * В данном классе определены следующие методы:
 *
 * 1. deleteByUserAndFilename(User user, String filename): Этот метод выполняет удаление файла из базы данных на основе
 * пользователя и имени файла. Аннотация @Modifying указывает, что это изменяющий запрос, а @Query указывает на запрос
 * на языке JPQL (Java Persistence Query Language), который выполняется для удаления записи из таблицы File.
 *
 * 2. findAllByUser(User user): Этот метод возвращает список всех файлов, принадлежащих определенному пользователю.
 *
 * 3. findByUserAndFilename(User user, String filename): Этот метод возвращает один файл на основе
 * пользователя и имени файла.
 *
 * 4. setNewFilenameByUserAndFilename(String newFilename, User user, String filename): Этот метод выполняет обновление
 * имени файла в базе данных на основе пользователя и текущего имени файла.
 * Аннотация @Modifying(clearAutomatically = true) указывает, что после выполнения запроса
 * сущности будут очищены из контекста персистентности.
 *
 * Класс FileRepository позволяет выполнять различные операции с файлами в базе данных, такие как добавление,
 * удаление и поиск файлов, а также обновление имени файла.
 */

public interface FileRepository extends JpaRepository<File, Long> {
    @Modifying
    @Query("DELETE FROM File f WHERE f.user = ?1 AND f.filename = ?2")
    int deleteByUserAndFilename(User user, String filename);

    List<File> findAllByUser(User user);

    File findByUserAndFilename(User user, String filename);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE File f SET f.filename = ?1 WHERE f.user = ?2 AND f.filename = ?3")
    int setNewFilenameByUserAndFilename(String newFilename, User user, String filename);
}
