package org.Test.util;

import org.slf4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    private static final SessionFactory SESSION_FACTORY;
    private static final StandardServiceRegistry REGISTRY;

    static {
        try {
            REGISTRY = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();

            SESSION_FACTORY = new MetadataSources(REGISTRY)
                    .buildMetadata()
                    .buildSessionFactory();

            log.info("SessionFactory успешно инициализирован");
        } catch (Throwable ex) {
            log.error("Ошибка инициализации SessionFactory", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        try {
            if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
                SESSION_FACTORY.close();
                log.info("SessionFactory закрыт");
            }
            if (REGISTRY != null) {
                StandardServiceRegistryBuilder.destroy(REGISTRY);
            }
        } catch (Exception e) {
            log.warn("Ошибка при закрытии SessionFactory", e);
        }
    }
}
