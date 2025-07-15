//package com.journal.backend.service;
//
//import com.journal.backend.entity.User;
//import com.journal.backend.repository.UserRepository;
//import com.journal.backend.services.CustomUserDetailsServiceImpl;
//import com.journal.backend.services.UserService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collections;
//
//import static org.mockito.Mockito.when;
//
//public class UserDetailsServiceImplTest {
//    @InjectMocks
//    private CustomUserDetailsServiceImpl userService;
//
////    @BeforeEach
//
//
//    @Mock
//    private UserRepository userRepository;
//    @Test
//    public void loadUserByUsernameTest(){
//        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(User.builder().username("manuj").password("manuj").roles(new ArrayList<>()).build());
//        UserDetails user=userService.loadUserByUsername("manuj");
//        Assertions.assertNotNull(user);
//    }
//}