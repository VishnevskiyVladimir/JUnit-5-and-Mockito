package com.training.junit.service;

import com.training.junit.dto.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    public void add(User user) {
        users.add(user);
    }
}

