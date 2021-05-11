package com.training.junit.dto;


import lombok.Value;

@Value(staticConstructor = "of")
public class User {
    private long id;
    private String username;
    private String password;

}
