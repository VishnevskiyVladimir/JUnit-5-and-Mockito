package com.training.junit.service;

import com.training.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserServiceTest {

    private static final User IVAN = User.of(1L, "Ivan", "pass1");
    private static final User PETR = User.of(2L, "Petr", "pass2");
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
        userService.add(IVAN);
        userService.add(PETR);
        var users = userService.getAll();
        assertEquals(2,users.size());
    }

    @Test
    void loginSucessIfUserExists(){
        System.out.println("test3" + this);
        userService.add(IVAN);
        Optional<User> loggedIn = userService.login(IVAN.getUsername(), IVAN.getPassword());
        assertTrue(loggedIn.isPresent());
        loggedIn.ifPresent(user ->  assertEquals(IVAN,user));
    }
    @Test
    void loginFailIfPasswordIsIncorrect(){
        System.out.println("test4" + this);
        userService.add(IVAN);
        Optional<User> loggedIn = userService.login(IVAN.getUsername(), "dummy");
        assertTrue(loggedIn.isEmpty());
    }
    @Test
    void loginFailIfUserDoesNotExist(){
        System.out.println("test4" + this);
        userService.add(IVAN);
        Optional<User> loggedIn = userService.login("dummy", IVAN.getPassword());
        assertTrue(loggedIn.isEmpty());
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
