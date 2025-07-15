//package com.journal.backend.service;
//
//import com.journal.backend.entity.User;
//import com.journal.backend.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class UserServiceTest {
//    @Autowired
//    private UserRepository userRepository;
//    @Test
//    public void add(){
//        assertEquals(5, 3+2);
//        User user=userRepository.findByUsername("manuj");
//        assertNotNull(user);
//        assertTrue(user.getJournalEntries().isEmpty());
//    }
//}