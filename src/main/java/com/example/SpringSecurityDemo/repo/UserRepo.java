package com.example.SpringSecurityDemo.repo;
import com.example.SpringSecurityDemo.models.Users;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;


@Repository
public interface UserRepo {
    Users saveUser(Users user);
    Users getUserById(String uid) throws ExecutionException, InterruptedException;
    void updateUser(Users user);
    void deleteUser(String uid);
    Users findByUsername(String username) throws ExecutionException, InterruptedException;

    String verify(Users user);
}
