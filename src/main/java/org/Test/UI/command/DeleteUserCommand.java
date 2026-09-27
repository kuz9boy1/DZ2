package org.Test.UI.command;

import org.Test.UI.ErrorHandler;
import org.Test.exeption.DaoException;
import org.Test.exeption.UserDao;
import org.Test.util.ConsoleInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteUserCommand implements Command {

    private static final Logger log = LoggerFactory.getLogger(DeleteUserCommand.class);

    private final UserDao userDao;
    private final ConsoleInput input;
    private final ErrorHandler errorHandler;

    public DeleteUserCommand(UserDao userDao, ConsoleInput input, ErrorHandler errorHandler) {
        this.userDao = userDao;
        this.input = input;
        this.errorHandler = errorHandler;
    }

    @Override
    public String key() { return "5"; }

    @Override
    public String description() { return "5. Удалить пользователя"; }

    @Override
    public void execute() {
        System.out.println("\n--- Удаление пользователя ---");
        Long id = input.readLong("id удаляемого пользователя: ");
        if (id == null) return;

        try {
            boolean deleted = userDao.deleteById(id);
            if (deleted) {
                System.out.println("Пользователь с id=" + id + " удалён.");
                log.info("Удалён пользователь id={}", id);
            } else {
                System.out.println("ℹ Пользователь с id=" + id + " не найден.");
            }
        } catch (DaoException e) {
            errorHandler.handle("удалении пользователя", e);
        }
    }
}