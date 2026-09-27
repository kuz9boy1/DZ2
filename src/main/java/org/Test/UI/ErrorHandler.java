package org.Test.UI;

import org.Test.exeption.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    public void handle(String operation, DaoException e) {
        log.error("Ошибка при {}: {}", operation, e.getMessage(), e);
        System.err.println("Ошибка при " + operation + ": " + e.getMessage());

        Throwable cause = e.getCause();
        if (cause == null) return;

        String msg = cause.getMessage() == null ? "" : cause.getMessage();
        if (msg.contains("повторный ключ") || msg.contains("unique")) {
            System.err.println("Нарушено ограничение уникальности (email уже существует).");
        } else if (msg.contains("Connection") || msg.contains("connect")) {
            System.err.println("Не удалось подключиться к PostgreSQL. Проверьте URL/логин/пароль и что сервер запущен.");
        } else if (msg.contains("relation") && msg.contains("does not exist")) {
            System.err.println("Таблица не существует. Проверьте hibernate.hbm2ddl.auto.");
        }
    }
}

