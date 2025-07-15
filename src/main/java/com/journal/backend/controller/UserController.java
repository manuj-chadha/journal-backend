package com.journal.backend.controller;


import com.journal.backend.entity.User;
import com.journal.backend.entity.WeatherResponse;
import com.journal.backend.services.UserService;
import com.journal.backend.services.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable ObjectId id){
        User user=userService.getUser(id);
        if(user!=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users=userService.getAllUsers();
        if(users!=null && !users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User newUser, @AuthenticationPrincipal UserDetails userDetails){
        User userInDb=userService.findByUsername(userDetails.getUsername());
        if(userInDb!=null){
            userService.updateUser(newUser, userInDb);
        }
        return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@AuthenticationPrincipal UserDetails userDetails){
        User userInDb=userService.findByUsername(userDetails.getUsername());
        if(userInDb!=null){
            userService.deleteUser(userDetails.getUsername());
        }
        return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
    }

    @GetMapping("/greeting")
    public ResponseEntity<?> greeting(@AuthenticationPrincipal UserDetails userDetails){
        System.out.println(userDetails.getUsername());
        User user=userService.findByUsername(userDetails.getUsername());
        System.out.println(user);
        String greeting="";
        WeatherResponse response= weatherService.getWeather("Kota");
        if(response!=null){
            greeting="Today weather feels like " + response.getCurrent().getFeelslike();
        }
        if(user!=null) return new ResponseEntity<>("Hii " + user.getUsername() + ". " + greeting, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}