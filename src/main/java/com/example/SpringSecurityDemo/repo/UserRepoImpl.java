package com.example.SpringSecurityDemo.repo;

import com.example.SpringSecurityDemo.models.Users;
import com.example.SpringSecurityDemo.service.JWTService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepoImpl implements UserRepo {

    private static final String COLLECTION_NAME = "users"; // Firestore Collection Name

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Override
    public Users saveUser(Users user) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION_NAME)
                .document(user.getUid()) // Use UID as document ID
                .set(user);
        return user;
    }

    @Override
    public Users getUserById(String uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection(COLLECTION_NAME)
                .document(uid)
                .get()
                .get();

        if (document.exists()) {
            return document.toObject(Users.class);
        } else {
            return null; // Handle case where user is not found
        }
    }
    @Override
    public Users findByUsername(String username) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                .whereEqualTo("username", username)
                .limit(1)  // Limit to one result since usernames should be unique
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            Users user = documents.get(0).toObject(Users.class);
            System.out.println("User found: " + user.getUsername()); // Debugging
            return user;
        } else {
            System.out.println("User not found for username: " + username); // Debugging
            return null;
        }
    }
    @Override
    public void updateUser(Users user) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION_NAME)
                .document(user.getUid())
                .set(user, SetOptions.merge()); // Merge updates with existing fields
    }

    @Override
    public void deleteUser(String uid) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION_NAME)
                .document(uid)
                .delete();
    }

    @Override
    public String verify(Users user){
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }
        return "Fail";
    }

}

