package com.training.junit.service;

import com.training.junit.dao.UserDao;
import com.training.junit.dto.User;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class UserService {

    private final List<User> users = new ArrayList<>();
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean delete(Long userId){
        return userDao.delete(25L);
    }

    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    public void add(User... users) {
        this.users.addAll(Arrays.asList(users));
    }

    public Optional<User> login(String username, String password) {

        if(username == null || password == null) {
            throw new IllegalArgumentException("Username or password should not be null");
        }
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .filter(user -> user.getPassword().equals(password))
                .findFirst();
    }

    public Map<Long, User> getUserMapById() {
        return users.stream()
                .collect(toMap(User::getId, Function.identity()));
    }
}

