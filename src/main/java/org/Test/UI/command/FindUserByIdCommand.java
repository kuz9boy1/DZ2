package org.Test.UI.command;

import org.Test.Entity.User;
import org.Test.UI.ErrorHandler;
import org.Test.exeption.DaoException;
import org.Test.exeption.UserDao;
import org.Test.util.ConsoleInput;

import java.util.Optional;

public class FindUserByIdCommand implements Command{

    private final UserDao userDao;
    private final ConsoleInput input;
    private final ErrorHandler errorHandler;

    public FindUserByIdCommand(UserDao userDao, ConsoleInput input, ErrorHandler errorHandler) {
        this.userDao = userDao;
        this.input = input;
        this.errorHandler = errorHandler;
    }

    @Override
    public String key() { return "2"; }

    @Override
    public String description() { return "2. Найти пользователя по id"; }

    @Override
    public void execute() {
        System.out.println("\n--- Поиск пользователя по id ---");
        Long id = input.readLong("id: ");
        if (id == null) return;

        try {
            Optional<User> user = userDao.findById(id);
            user.ifPresentOrElse(
                    u -> System.out.println("Найден: " + u),
                    () -> System.out.println("ℹ Пользователь с id=" + id + " не найден.")
            );
        } catch (DaoException e) {
            errorHandler.handle("поиске пользователя", e);
        }
    }
}
