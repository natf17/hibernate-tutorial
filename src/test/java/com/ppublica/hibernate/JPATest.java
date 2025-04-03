package com.ppublica.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class JPATest {

    @AfterEach
    public void cleanup() {
        JPA.closeEntityManagerFactory();
    }
    
    @Test
    public void insertAndReadUserSuccess() {
        
        User testUser = new User();
        testUser.setUsername("test_user");
        testUser.setPassword("test_psswd");

        User foundUser = null;


        JPA.insertUser(testUser);
        foundUser = JPA.readUser(testUser.getId());

        assertNotNull(testUser.getId());
        assertEquals(testUser.getUsername(),foundUser.getUsername()); 
        assertEquals(testUser.getPassword(),foundUser.getPassword());

        
    }

    
}
