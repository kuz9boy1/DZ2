package org.Test;

import org.Test.UI.ConsoleMenu;
import org.Test.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Запуск консольного приложения User CRUD");
        try (ConsoleMenu menu = new ConsoleMenu()) {
            menu.run();
        } catch (Exception e) {
            log.error("Критическая ошибка приложения", e);
            System.err.println("Критическая ошибка: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
            log.info("Приложение остановлено");
        }
    }
}
