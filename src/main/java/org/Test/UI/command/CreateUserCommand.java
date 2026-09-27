package org.Test.UI.command;

import org.Test.Entity.User;
import org.Test.UI.ErrorHandler;
import org.Test.exeption.DaoException;
import org.Test.exeption.UserDao;
import org.Test.util.ConsoleInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUserCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(CreateUserCommand.class);

    private final UserDao userDao;
    private final ConsoleInput input;
    private final ErrorHandler errorHandler;

    public CreateUserCommand(UserDao userDao, ConsoleInput input, ErrorHandler errorHandler) {
        this.userDao = userDao;
        this.input = input;
        this.errorHandler = errorHandler;
    }

    @Override
    public String key() { return "1"; }

    @Override
    public String description() { return "1. Создать пользователя"; }

    @Override
    public void execute() {
        System.out.println("\n--- Создание пользователя ---");
        String name = input.readLine("Имя: ");
        String email = input.readLine("Email: ");
        Integer age = input.readInt("Возраст: ");

        if (name.isBlank() || email.isBlank() || age == null) {
            System.out.println("Все поля обязательны для заполнения.");
            return;
        }

        try {
            User created = userDao.create(new User(name, email, age));
            System.out.println("Пользователь создан: " + created);
            log.info("Создан пользователь: {}", created);
        } catch (DaoException e) {
            errorHandler.handle("создании пользователя", e);
        }
    }
}
