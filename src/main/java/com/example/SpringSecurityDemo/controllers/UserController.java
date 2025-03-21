package com.example.SpringSecurityDemo.controllers;


import com.example.SpringSecurityDemo.models.Users;
import com.example.SpringSecurityDemo.repo.UserRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @Autowired
    private UserRepoImpl userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user){

        return userService.verify(user);

    }
}
