package com.example.SpringSecurityDemo.models;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private String uid;
    private String username;
    private String password;

}
