package org.Test.UI.command;

import org.Test.Entity.User;
import org.Test.UI.ErrorHandler;
import org.Test.exeption.DaoException;
import org.Test.exeption.UserDao;
import org.Test.util.ConsoleInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UpdateUserCommand implements Command {

    private static final Logger log = LoggerFactory.getLogger(UpdateUserCommand.class);

    private final UserDao userDao;
    private final ConsoleInput input;
    private final ErrorHandler errorHandler;

    public UpdateUserCommand(UserDao userDao, ConsoleInput input, ErrorHandler errorHandler) {
        this.userDao = userDao;
        this.input = input;
        this.errorHandler = errorHandler;
    }

    @Override
    public String key() { return "4"; }

    @Override
    public String description() { return "4. Обновить пользователя"; }

    @Override
    public void execute() {
        System.out.println("\n--- Обновление пользователя ---");
        Long id = input.readLong("id обновляемого пользователя: ");
        if (id == null) return;

        try {
            Optional<User> existing = userDao.findById(id);
            if (existing.isEmpty()) {
                System.out.println("ℹ Пользователь с id=" + id + " не найден.");
                return;
            }
            User user = existing.get();
            System.out.println("Текущие данные: " + user);

            String name = input.readLine("Новое имя (" + user.getName() + "): ");
            String email = input.readLine("Новый email (" + user.getEmail() + "): ");
            String ageStr = input.readLine("Новый возраст (" + user.getAge() + "): ");

            if (!name.isBlank()) user.setName(name);
            if (!email.isBlank()) user.setEmail(email);
            if (!ageStr.isBlank()) {
                try {
                    user.setAge(Integer.parseInt(ageStr.trim()));
                } catch (NumberFormatException ex) {
                    System.out.println("Возраст должен быть числом, оставлено прежнее значение.");
                }
            }

            User updated = userDao.update(user);
            System.out.println("Пользователь обновлён: " + updated);
            log.info("Обновлён пользователь: {}", updated);
        } catch (DaoException e) {
            errorHandler.handle("обновлении пользователя", e);
        }
    }
}