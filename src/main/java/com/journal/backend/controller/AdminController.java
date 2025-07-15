package com.journal.backend.controller;

import com.journal.backend.entity.User;
import com.journal.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AppCache appCache;
    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        List<User> users=userService.getAllUsers();
        if(users!=null && !users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/create-admin-user")
    public void createAdminUser(User user){
        userService.createAdminUser(user);
    }
    @GetMapping("clear-app-cache")
    public void clearAppCache(){
        appCache.init();
    }
}