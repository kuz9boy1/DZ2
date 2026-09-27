package org.Test.UI.command;

import org.Test.Entity.User;
import org.Test.UI.ErrorHandler;
import org.Test.exeption.DaoException;
import org.Test.exeption.UserDao;
import org.Test.util.ConsoleInput;

import java.util.List;

public class FindUsersByAgeCommand implements Command {

    private final UserDao userDao;
    private final ConsoleInput input;
    private final ErrorHandler errorHandler;

    public FindUsersByAgeCommand(UserDao userDao, ConsoleInput input, ErrorHandler errorHandler) {
        this.userDao = userDao;
        this.input = input;
        this.errorHandler = errorHandler;
    }

    @Override
    public String key() { return "6"; }

    @Override
    public String description() { return "6. Найти пользователей старше N лет"; }

    @Override
    public void execute() {
        System.out.println("\n--- Пользователи старше N лет ---");
        Integer age = input.readInt("Возраст: ");
        if (age == null) return;

        try {
            List<User> users = userDao.findByAgeGreaterThan(age);
            if (users.isEmpty()) {
                System.out.println("ℹ Ничего не найдено.");
            } else {
                users.forEach(System.out::println);
            }
        } catch (DaoException e) {
            errorHandler.handle("фильтрации пользователей", e);
        }
    }
}