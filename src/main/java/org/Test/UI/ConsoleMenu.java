package org.Test.UI;

import org.Test.UI.command.*;
import org.Test.exeption.UserDao;
import org.Test.exeption.UserDaoImpl;
import org.Test.util.ConsoleInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConsoleMenu implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(ConsoleMenu.class);
    private static final String EXIT_KEY = "0";

    private final ConsoleInput input;
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public ConsoleMenu() {
        this.input = new ConsoleInput();
        UserDao userDao = new UserDaoImpl();
        ErrorHandler errorHandler = new ErrorHandler();

        register(new CreateUserCommand(userDao, input, errorHandler));
        register(new FindUserByIdCommand(userDao, input, errorHandler));
        register(new FindAllUsersCommand(userDao, errorHandler));
        register(new UpdateUserCommand(userDao, input, errorHandler));
        register(new DeleteUserCommand(userDao, input, errorHandler));
        register(new FindUsersByAgeCommand(userDao, input, errorHandler));
    }

    public void register(Command command) {
        commands.put(command.key(), command);
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = input.readLine("Выберите пункт: ");

            if (EXIT_KEY.equals(choice)) {
                running = false;
                continue;
            }

            Command command = commands.get(choice);
            if (command == null) {
                System.out.println(" Неверный пункт меню. Попробуйте снова.");
                log.warn("Пользователь ввёл неизвестный пункт меню: '{}'", choice);
                continue;
            }

            command.execute();
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("USER CRUD (Hibernate)");
        for (Command c : commands.values()) {
            System.out.println(c.description());
        }
        System.out.println("0. Выход");
    }

    @Override
    public void close() {
        input.close();
    }
}
