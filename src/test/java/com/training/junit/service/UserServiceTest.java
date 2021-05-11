package com.training.junit.service;

import com.training.junit.dto.User;
import org.junit.jupiter.api.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    private UserService userService;

    @BeforeAll
    static void deleteDataFromDb() {
        System.out.println("Before All");
    }

    @BeforeEach
    void closeConnectionPool() {
        System.out.println("Before Each" + this);
        userService = new UserService();
    }

    @Test
    void usersEmptyIfNoUsersAdded(){
        System.out.println("test1" + this);
        var users = userService.getAll();
        Assertions.assertTrue(users.isEmpty());
    }

    @Test
    void usersSizeIfUserAdded(){
        System.out.println("test2" + this);
        userService.add(new User());
        userService.add(new User());
        var users = userService.getAll();
        Assertions.assertEquals(2,users.size());
    }

    @AfterEach
    void clean() {
        System.out.println("After Each" + this);
    }

    @AfterAll
    static void cleanFinal() {
        System.out.println("After All");
    }
}
