package ru.netology.cloudstorage.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.exceptions.*;
import ru.netology.cloudstorage.models.File;
import ru.netology.cloudstorage.models.User;
import ru.netology.cloudstorage.repositories.AuthRepository;
import ru.netology.cloudstorage.repositories.FileRepository;
import ru.netology.cloudstorage.request.RequestEditFileName;
import ru.netology.cloudstorage.response.ResponseFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Этот код представляет собой сервисный класс FileService, который предоставляет методы для работы с
 * файлами в облачном хранилище.
 * <p>
 * Вот краткое описание каждого метода:
 * <p>
 * - uploadFile: загружает файл в облачное хранилище. Проверяет, что пользователь авторизован,
 * создает новый объект File с указанным именем файла, текущим временем, размером и содержимым файла,
 * а затем сохраняет его в репозитории файлов.
 * <p>
 * - deleteFile: удаляет файл из облачного хранилища. Проверяет, что пользователь авторизован, проверяет,
 * что имя файла не пусто, затем удаляет файл из репозитория по имени пользователя и имени файла.
 * Если удаление не удалось (например, файл не найден), выбрасывается исключение ErrorDeleteFile.
 * <p>
 * - downloadFile: загружает содержимое файла из облачного хранилища. Проверяет, что пользователь авторизован,
 * находит файл в репозитории по имени пользователя и имени файла, а затем возвращает его содержимое в
 * виде массива байтов. Если файл не найден или его содержимое пусто, выбрасывается исключение ErrorInputData
 * или ErrorUploadFile соответственно.
 * <p>
 * - editFileName: изменяет имя файла в облачном хранилище. Проверяет, что пользователь авторизован,
 * находит файл в репозитории по имени пользователя и имени файла, а затем изменяет его имя на новое имя из запроса
 * RequestEditFileName. Если имя файла не изменилось, выбрасывается исключение ErrorUploadFile.
 * <p>
 * - getAllFiles: возвращает список всех файлов пользователя в облачном хранилище. Проверяет, что пользователь
 * авторизован, проверяет, что лимит не равен нулю, затем находит все файлы пользователя в репозитории и
 * возвращает список ResponseFile, содержащий имена и размеры файлов.
 * <p>
 * Класс FileService также имеет конструктор, который принимает репозитории AuthRepository и FileRepository
 * в качестве зависимостей.
 * <p>
 * Данный метод getUserByToken принимает в качестве аргумента строку authToken и возвращает объект типа User.
 * <p>
 * Метод проверяет, начинается ли переданная строка authToken с подстроки "Bearer ". Если это условие выполняется,
 * метод обрезает строку authToken, удаляя первые 7 символов, и сохраняет результат в переменную tokenWithoutBearer.
 * <p>
 * Затем метод вызывает метод getAuthenticationUserByToken из объекта authRepository, передавая ему значение
 * tokenWithoutBearer. Результат этого вызова возвращается из метода getUserByToken.
 * <p>
 * Если переданная строка authToken не начинается с "Bearer ", то метод возвращает null.
 */
@Service
@Transactional
public class FileService {
    final static Logger logger = Logger.getLogger(FileService.class);
    private final AuthRepository authRepository;
    private final FileRepository fileRepository;

    @Autowired
    public FileService(AuthRepository authRepository, FileRepository fileRepository) {
        this.authRepository = authRepository;
        this.fileRepository = fileRepository;
    }

    public boolean uploadFile(String authToken, String filename, MultipartFile multipartFile) {
        User user = getUserByToken(authToken);
        if (user == null) {
            logger.error("User is not found, no authorization!");
            throw new UnauthorizedExceptionError();
        }
        try {
            File uploadFile = new File(filename, LocalDateTime.now(), multipartFile.getSize(), multipartFile.getBytes(), user);
            fileRepository.save(uploadFile);
            logger.info(String.format("uploadFile: %s ", uploadFile.getFilename()));
        } catch (IOException e) {
            logger.error("uploadFile warn: ", e);
            throw new InputDataExceptionError();

        }
        return true;
    }

    public void deleteFile(String authToken, String filename) {
        User user = getUserByToken(authToken);
        if (user == null) {
            logger.error("User is not found, no authorization!");
            throw new UnauthorizedExceptionError();
        }
        if (StringUtils.isEmpty(filename)) {
            logger.error("Invalid input data!");
            throw new InputDataExceptionError();
        }
        long deletedCount = fileRepository.deleteByUserAndFilename(user, filename);
        if (deletedCount == 0) {
            logger.error("Error when deleting a file!");
            throw new DeleteFileExceptionError();
        }
        logger.info(String.format("Deleted file: %s ", filename));
    }

    public byte[] downloadFile(String authToken, String filename) {
        User user = getUserByToken(authToken);
        if (user == null) {
            logger.error("User is not found, no authorization!");
            throw new UnauthorizedExceptionError();
        }
        File file = fileRepository.findByUserAndFilename(user, filename);
        if (file == null) {
            logger.error("File not found, incorrect input data! ");
            throw new InputDataExceptionError();
        }
        byte[] fileContent = file.getFileContent();
        if (fileContent == null) {
            logger.error("Error loading file.");
            throw new UploadFileExceptionError();
        }
        logger.info(String.format("Download file: %s ", filename));
        return fileContent;
    }

    public void editFileName(String authToken, String filename, RequestEditFileName requestEditFileName) {
        User user = getUserByToken(authToken);
        if (user == null) {
            logger.error("User is not found, no authorization!");
            throw new UnauthorizedExceptionError();
        }
        File file = fileRepository.findByUserAndFilename(user, filename);
        if (file == null) {
            logger.error("File not found, incorrect input data!");
            throw new InputDataExceptionError();
        }
        fileRepository.setNewFilenameByUserAndFilename(requestEditFileName.getFilename(), user, filename);
        if (filename.equals(requestEditFileName.getFilename())) {
            logger.error("File name has not changed, file download error!");
            throw new UploadFileExceptionError();
        }
        logger.info(String.format("Edit file name: %s ", filename));
    }

    public List<ResponseFile> getAllFiles(String authToken, Integer limit) {
        User user = getUserByToken(authToken);
        if (user == null) {
            logger.warn("User is not found, no authorization!");
            throw new UnauthorizedExceptionError();
        }
        if (limit == 0) {
            logger.error("Invalid input data!");
            throw new InputDataExceptionError();
        }
        List<File> allFilesByUser = fileRepository.findAllByUser(user);
        if (allFilesByUser == null) {
            logger.error("Error when retrieving a list of files!");
            throw new GettingFileListExceptionError();
        }
        return allFilesByUser.stream().map(x -> new ResponseFile(x.getFilename(), x.getSize())).toList();
    }

    public User getUserByToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            String tokenWithoutBearer = authToken.substring(7);
            return authRepository.getAuthenticationUserByToken(tokenWithoutBearer);
        } else return null;
    }
}