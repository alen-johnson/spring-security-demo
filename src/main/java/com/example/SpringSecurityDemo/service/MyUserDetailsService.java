package com.example.SpringSecurityDemo.service;

import com.example.SpringSecurityDemo.models.UserPrincipal;
import com.example.SpringSecurityDemo.models.Users;
import com.example.SpringSecurityDemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            Users user = repo.findByUsername(username);
            if(user== null){
                System.out.println("User not found");
                throw  new UsernameNotFoundException("user not found");
            }

            return new UserPrincipal(user);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
