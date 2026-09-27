package org.Test.exeption;

import org.Test.Entity.User;
import org.Test.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class UserDaoImpl implements UserDao{

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User create(User user) {
        log.debug("create: {}", user);
        return inTransaction(session -> {
            session.persist(user);
            return user;
        });
    }

    @Override
    public Optional<User> findById(Long id) {
        log.debug("findById: id={}", id);
        if (id == null) {
            return Optional.empty();
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            log.error("Ошибка при поиске пользователя id={}", id, e);
            throw new DaoException("Не удалось найти пользователя с id=" + id, e);
        }
    }

    @Override
    public List<User> findAll() {
        log.debug("findAll");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User u order by u.id", User.class)
                    .getResultList();
        } catch (HibernateException e) {
            log.error("Ошибка при получении списка пользователей", e);
            throw new DaoException("Не удалось получить список пользователей", e);
        }
    }

    @Override
    public User update(User user) {
        log.debug("update: {}", user);
        if (user.getId() == null) {
            throw new DaoException("Невозможно обновить пользователя без id");
        }
        return inTransaction(session -> {
            User existing = session.get(User.class, user.getId());
            if (existing == null) {
                throw new DaoException("Пользователь с id=" + user.getId() + " не найден");
            }
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            existing.setAge(user.getAge());
            return existing;
        });
    }

    @Override
    public boolean deleteById(Long id) {
        log.debug("deleteById: id={}", id);
        if (id == null) {
            return false;
        }
        return inTransaction(session -> {
            User user = session.get(User.class, id);
            if (user == null) {
                return false;
            }
            session.remove(user);
            return true;
        });
    }

    @Override
    public List<User> findByAgeGreaterThan(int age) {
        log.debug("findByAgeGreaterThan: age={}", age);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User u where u.age > :age order by u.age", User.class)
                    .setParameter("age", age)
                    .getResultList();
        } catch (HibernateException e) {
            log.error("Ошибка при фильтрации пользователей по возрасту", e);
            throw new DaoException("Не удалось выполнить фильтрацию по возрасту", e);
        }
    }
    private <T> T inTransaction(Function<Session, T> action) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            try {
                T result = action.apply(session);
                tx.commit();
                return result;
            } catch (RuntimeException e) {
                safeRollback(tx);
                throw e;
            }
        } catch (HibernateException e) {
            log.error("Ошибка Hibernate при выполнении транзакции", e);
            throw new DaoException("Ошибка при работе с базой данных: " + e.getMessage(), e);
        }
    }

    private void safeRollback(Transaction tx) {
        try {
            if (tx != null && tx.isActive()) {
                tx.rollback();
                log.warn("Транзакция откачена (rollback)");
            }
        } catch (HibernateException rollbackEx) {
            log.error("Не удалось выполнить rollback", rollbackEx);
        }
    }
}
