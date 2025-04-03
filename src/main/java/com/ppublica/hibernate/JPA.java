package com.ppublica.hibernate;

import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JPA {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test-jpa");

    public static void insertUser(User user) {

        inTransaction(entityManager -> {
            entityManager.persist(user);
        });

    }

    public static User readUser(Long id) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager.find(User.class, id);
        }
        finally {
            entityManager.close();
        }

    }

    public static void deleteUser(User user) {

        inTransaction(entityManager -> {
            entityManager.remove(user);
        });
    }


    static void inTransaction(Consumer<EntityManager> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            entityManager.close();
        }

    }

    static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }


}
