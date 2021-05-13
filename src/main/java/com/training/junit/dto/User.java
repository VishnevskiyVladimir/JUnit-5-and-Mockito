package com.training.junit.dto;


import lombok.Value;

@Value(staticConstructor = "of")
public class User {
    long id;
    String username;
    String password;

}
