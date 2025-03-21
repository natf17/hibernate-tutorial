package com.ppublica.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class NativeHibernateTest {

    @AfterEach
    public void cleanup() {
        NativeHibernate.closeSessionFactory();
    }
    
    @Test
    public void insertAndReadUserSuccessWhenConfigureWithFile() {
        NativeHibernate.configureWithFile();
        
        User testUser = new User();
        testUser.setUsername("test_user");
        testUser.setPassword("test_psswd");

        User foundUser = null;


        NativeHibernate.insertUser(testUser);
        foundUser = NativeHibernate.readUser(testUser.getId());

        assertNotNull(testUser.getId());
        assertEquals(testUser.getUsername(),foundUser.getUsername()); 
        assertEquals(testUser.getPassword(),foundUser.getPassword());

        
    }

    @Test
    public void insertAndReadUserSuccessWhenConfigureProgrammatically() {
        NativeHibernate.configureProgrammatically();

        
        User testUser = new User();

        testUser.setUsername("test_user");
        testUser.setPassword("test_psswd");

        User foundUser = null;


        NativeHibernate.insertUser(testUser);
        foundUser = NativeHibernate.readUser(testUser.getId());

        assertNotNull(testUser.getId());
        assertEquals(testUser.getUsername(),foundUser.getUsername()); 
        assertEquals(testUser.getPassword(),foundUser.getPassword());
        
    }

}
