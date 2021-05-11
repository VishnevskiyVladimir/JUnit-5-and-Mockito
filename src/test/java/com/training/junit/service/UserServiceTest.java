package com.training.junit.service;

import com.training.junit.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class UserServiceTest {

    @Test
    void usersEmptyIfNoUsersAdded(){
        var userService = new UserService();
        var users = userService.getAll();
        Assertions.assertTrue(users.isEmpty());
    }
}
