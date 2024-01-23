package ru.netology.cloudstorage.exceptions;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super("Error unauthorized");
    }
}