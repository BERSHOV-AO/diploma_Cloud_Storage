package ru.netology.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.request.RequestEditFileName;
import ru.netology.cloudstorage.response.ResponseFile;
import ru.netology.cloudstorage.services.FileService;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Данный класс представляет контроллер для обработки HTTP-запросов, связанных с операциями над файлами в облачном
 * хранилище.
 * <p>
 * Класс FileStorageController является компонентом Spring, помеченным аннотацией @RestController, что указывает на то,
 * что этот класс является контроллером и будет обрабатывать HTTP-запросы. Внутри контроллера определены несколько
 * методов, каждый из которых обрабатывает определенный тип запроса.
 * <p>
 * 1. Метод uploadFile обрабатывает POST-запрос на загрузку файла. Он принимает заголовок auth-token, параметр filename
 * и сам файл file. Загруженный файл передается сервису fileService для выполнения соответствующих операций.
 * Данная строка представляет собой объявление метода uploadFile, который будет обрабатывать POST-запрос на
 * загрузку файла.
 * <p>
 * Аннотация @PostMapping("/file") указывает, что этот метод будет обрабатывать только POST-запросы
 * с URL-адресом "/file".
 * Аргумент @RequestHeader("auth-token") указывает на то, что входной параметр authToken будет получен из заголовка
 * запроса с именем "auth-token".
 * Аргумент @RequestParam("filename") указывает на то, что входной параметр filename будет получен из параметра
 * запроса с именем "filename".
 * Параметр MultipartFile file представляет сам загружаемый файл. Он будет автоматически привязан к загруженному
 * файлу из запроса.
 * <p>
 * 2. Метод deleteFile обрабатывает DELETE-запрос на удаление файла. Он принимает заголовок auth-token и параметр
 * filename. Удаление файла также делегируется сервису fileService.
 * <p>
 * 3. Метод downloadFile обрабатывает GET-запрос на скачивание файла. Он принимает заголовок auth-token и параметр
 * filename. Сервис fileService выполняет операцию загрузки файла и возвращает его в виде массива байтов.
 * <p>
 * 4. Метод editFile обрабатывает PUT-запрос на изменение имени файла. Он принимает заголовок auth-token, параметр
 * filename и тело запроса requestEditFileName, содержащее новое имя файла. Сервис fileService выполняет операцию
 * изменения имени файла.
 * <p>
 * 5. Метод getAllFiles обрабатывает GET-запрос на получение списка всех файлов. Он принимает заголовок auth-token
 * и параметр limit, указывающий максимальное количество файлов, которые нужно вернуть. Сервис fileService выполняет
 * операцию получения списка файлов и возвращает список объектов ResponseFile.
 * <p>
 * Каждый метод контроллера возвращает объект ResponseEntity, который представляет ответ HTTP-запроса. В случае
 * успешного выполнения операции, возвращается статус HttpStatus.OK. Если операция завершилась с ошибкой, можно
 * вернуть другой статус и соответствующее сообщение об ошибке.
 * <p>
 * Все методы контроллера используют сервис fileService, который выполняет бизнес-логику операций над файлами,
 * такие как загрузка, удаление, скачивание и т.д.
 */

@RestController
public class FileStorageController {

    private final FileService fileService;

    @Autowired
    public FileStorageController(FileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String filename,
                                        MultipartFile file) {
        fileService.uploadFile(authToken, filename, file);
        return new ResponseEntity<>("Success upload", HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String filename) {
        fileService.deleteFile(authToken, filename);
        return new ResponseEntity<>("Success delete", HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(@RequestHeader("auth-token") String authToken,
                                          @RequestParam("filename") String filename) {
        byte[] file = fileService.downloadFile(authToken, filename);
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @PutMapping("/file")
    public ResponseEntity<?> editFile(@RequestHeader("auth-token") String authToken,
                                      @RequestParam("filename") String filename,
                                      @RequestBody RequestEditFileName requestEditFileName) {
        fileService.editFileName(authToken, filename, requestEditFileName);
        return new ResponseEntity<>("Edit file name", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllFiles(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("limit") Integer limit) {
        List<ResponseFile> rp = fileService.getAllFiles(authToken, limit);
        return new ResponseEntity<>(rp, HttpStatus.OK);
    }
}