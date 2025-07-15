package com.journal.backend.services;

import com.journal.backend.dto.UserDTO;
import com.journal.backend.entity.Collection;
import com.journal.backend.entity.User;
import com.journal.backend.repository.CollectionRepo;
import com.journal.backend.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CollectionRepo collectionRepo;

    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    public User getUser(ObjectId id){
        return userRepository.findById(id).orElse(null);
    }

    public void updateUser(User newDetails, User user) {
        user.setPassword((!newDetails.getPassword().isEmpty()) ? encoder.encode(newDetails.getPassword()) : user.getPassword());
        user.setUsername((!newDetails.getUsername().isEmpty()) ? newDetails.getUsername() : user.getUsername());
        userRepository.save(user);
    }
    public void createNewUser(UserDTO data) throws Exception {
        validateUsername(data.getUsername());

        User user = User.builder()
                .username(data.getUsername())
                .email(data.getEmail())
                .password(encoder.encode(data.getPassword()))
                .roles(List.of("USER"))
                .build();

        user = userRepository.save(user);

        Collection unorganized = Collection.builder()
                .title("Unorganized")
                .user(user.getId())
                .entries(new ArrayList<>())
                .build();

        collectionRepo.save(unorganized);
    }


    private void validateUsername(String username) {
        User user=userRepository.findByUsername(username);
        if(user!=null) throw new IllegalArgumentException("username already exists.");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);


    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteUserByUsername(username);
    }

    public void createAdminUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
    }
}