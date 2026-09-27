package org.Test.UI.command;

import org.Test.Entity.User;
import org.Test.UI.ErrorHandler;
import org.Test.exeption.DaoException;
import org.Test.exeption.UserDao;

import java.util.List;

public class FindAllUsersCommand implements Command {
    private final UserDao userDao;
    private final ErrorHandler errorHandler;

    public FindAllUsersCommand(UserDao userDao, ErrorHandler errorHandler) {
        this.userDao = userDao;
        this.errorHandler = errorHandler;
    }

    @Override
    public String key() { return "3"; }

    @Override
    public String description() { return "3. Показать всех пользователей"; }

    @Override
    public void execute() {
        System.out.println("\n--- Список всех пользователей ---");
        try {
            List<User> users = userDao.findAll();
            if (users.isEmpty()) {
                System.out.println("Список пуст.");
            } else {
                users.forEach(System.out::println);
                System.out.println("Всего записей: " + users.size());
            }
        } catch (DaoException e) {
            errorHandler.handle("получении списка пользователей", e);
        }
    }
}
