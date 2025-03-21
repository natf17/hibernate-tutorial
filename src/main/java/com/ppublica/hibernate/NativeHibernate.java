package com.ppublica.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_PASSWORD;
import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_URL;
import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_USER;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;

import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

import org.hibernate.Session;

public class NativeHibernate {

    private static SessionFactory sessionFactory;


    public static void insertUser(User user) {

        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
		session.persist(user);
		session.getTransaction().commit();

    }

    public static User readUser(Long id) {

        Session session = sessionFactory.openSession();
        User user = session.get(User.class, id);        
        session.close();

        return user;

    }

    public static void deleteUser(User user) {

        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
		session.persist(user);
		session.getTransaction().commit();

    }


    public static void configureProgrammatically() {
        closeSessionFactory();

        Configuration config = new Configuration();

        sessionFactory = config.addAnnotatedClass(User.class)
            .setProperty(JAKARTA_JDBC_URL, "jdbc:hsqldb:mem:testdb1")
            .setProperty(JAKARTA_JDBC_USER, "SA")
            .setProperty(JAKARTA_JDBC_PASSWORD, "")
            .setProperty(SHOW_SQL, "true")
            .setProperty(FORMAT_SQL, "true")
            .setProperty(HIGHLIGHT_SQL, "true")
            .setProperty(HBM2DDL_AUTO, "create-drop")
            .buildSessionFactory();

        //sessionFactory.getSchemaManager().exportMappedObjects(true);

    }

    
    public static void configureWithFile() {
        closeSessionFactory();

        // assume properties are in hibernate.properties, or in hibernate.cfg.xml
        // pretty much what Configuration does under the hood:

        // looks for hibernate.properties of .cfg.xml by defaukt
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();    

        try {
            sessionFactory =
                new MetadataSources(registry)             
                        .addAnnotatedClass(User.class)   
                        .buildMetadata()                  
                        .buildSessionFactory();           
        }
    
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void closeSessionFactory() {
        if(sessionFactory != null) {
            sessionFactory.close();
        }
    }

}
