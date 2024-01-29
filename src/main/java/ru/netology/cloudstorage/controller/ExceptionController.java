package ru.netology.cloudstorage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.cloudstorage.exceptions.*;
import ru.netology.cloudstorage.response.ResponseError;


/**
 * Данный класс является контроллером, который обрабатывает исключения, возникающие в приложении, и возвращает
 * соответствующие ответы с ошибками.
 * Внутри класса определены несколько методов-обработчиков, каждый из которых отвечает за обработку конкретного
 * типа исключения. Каждый метод-обработчик имеет аннотацию @ExceptionHandler, которая указывает, что данный метод
 * должен быть вызван при возникновении соответствующего исключения.
 * <p>
 * Каждый метод-обработчик создает объект ResponseError, который представляет собой объект с информацией об ошибке.
 * Затем создается объект ResponseEntity, который содержит тело ответа (объект errorResponse) и статус ответа
 * (объект HttpStatus).
 * <p>
 * Например, если возникает исключение BadCredentialsExceptionError, вызывается метод handlerBadCredentials().
 * Внутри этого метода создается объект ResponseError с сообщением об ошибке "Error Bad Credentials" и кодом ошибки 0.
 * Затем создается объект ResponseEntity, в котором передается объект errorResponse и статус
 * ответа HttpStatus.BAD_REQUEST. Этот объект ResponseEntity будет возвращен клиенту как ответ на запрос.
 * <p>
 * Таким образом, данный класс позволяет обрабатывать исключения и возвращать соответствующие ответы с ошибками,
 * что улучшает обработку ошибок в приложении.
 */
@Controller
public class ExceptionController {

    @ExceptionHandler(BadCredentialsExceptionError.class)
    public ResponseEntity<?> handlerBadCredentials() {
        ResponseError errorResponse = new ResponseError("Error Bad Credentials", 0);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedExceptionError.class)
    public ResponseEntity<?> handlerErrorUnauthorized() {
        ResponseError errorResponse = new ResponseError("Error Unauthorized", 0);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InputDataExceptionError.class)
    public ResponseEntity<?> handlerErrorInputData() {
        ResponseError errorResponse = new ResponseError("Error Input Data", 0);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeleteFileExceptionError.class)
    public ResponseEntity<?> handlerDeleteFile() {
        ResponseError errorResponse = new ResponseError("Error Delete File", 0);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UploadFileExceptionError.class)
    public ResponseEntity<?> handlerUploadFile() {
        ResponseError errorResponse = new ResponseError("Error Upload File", 0);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
