package com.ppublica.hibernate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test an in-process HSQLDB server programatically with pure JDBC.
 */
public class HSQLDBTest {

    @BeforeAll
    static void start() {
        // set initial properties
        HsqlProperties p = new HsqlProperties();
        p.setProperty("server.database.0","mem:testdb");
        p.setProperty("server.dbname.0","testdb");

        Server server = new Server();
        server.setErrWriter(null);
        server.setLogWriter(null);

        try {
            server.setProperties(p);

        } catch (AclFormatException e) {
            System.err.println("ERROR: AclFormatException");
        } catch (IOException ex) {
            System.err.println("ERROR: IOException when setting HSQLDB server properties");
        }

        server.start();


        
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver" );
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            return;
        }

        // connect and create table
        try (Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb;ifexists=true", "SA", "");){
            Statement statement = connection.createStatement();
            System.out.println("Obtained connection");
            statement.executeQuery("CREATE table users (id INTEGER PRIMARY KEY, username VARCHAR(20), password VARCHAR(20))");
        } catch(SQLException ex) {
            System.err.println("ERROR: SQL Exception while connecting to database.");
        }

        
    }

    @AfterAll
    static void end() {
        // connect to database and shut down
        try (Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb;ifexists=true", "SA", "");){
            Statement statement = connection.createStatement();
            System.out.println("Shutting down connection");
            statement.executeQuery("SHUTDOWN");
        } catch(SQLException ex) {
            System.err.println("ERROR: SQL Exception while connecting to database.");
        }
    }

    /**
     * 
     */
    @Test
    public void insertUserSuccess() {
        User testUser = new User(1L, "test_user","test_psswd");
        User foundUser = null;

        // connect to database and insert user. Then read it
        try (Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb;ifexists=true", "SA", "");){
            Statement statement = connection.createStatement();
            System.out.println("Inserting user");
            statement.executeQuery("INSERT INTO users VALUES (" 
                                    + testUser.id() + ", " 
                                    + "'" + testUser.username() + "', "
                                    + "'" + testUser.password() + "')");
            connection.commit();
            System.out.println("Inserted user");

            ResultSet rs = statement.executeQuery("SELECT id, username, password FROM users WHERE id = " + testUser.id());
            if(rs.next()) {
                System.out.println("Found user");
                Long id = rs.getLong(1);
                String username = rs.getString(2);
                String password = rs.getString(3);
                foundUser = new User(id, username, password);
            }
            
        } catch(SQLException ex) {
            ex.printStackTrace();
            System.err.println("ERROR: SQL Exception while connecting to database.");
        }

        assertEquals(testUser,foundUser);
        
    }

    record User(Long id, String username, String password) {}
}
