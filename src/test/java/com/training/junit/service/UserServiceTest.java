package com.training.junit.service;

import com.training.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("user")
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
    void usersEmptyIfNoUsersAdded() {
        System.out.println("test1" + this);
        var users = userService.getAll();
        assertThat(users).isEmpty();
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("test2" + this);
        userService.add(IVAN, PETR);
        var users = userService.getAll();
        assertThat(users).hasSize(2);
    }

    @Test
    @Tag("login")
    void loginSucessIfUserExists() {
        System.out.println("test3" + this);
        userService.add(IVAN);
        Optional<User> loggedIn = userService.login(IVAN.getUsername(), IVAN.getPassword());
        assertThat(loggedIn).isPresent();
        loggedIn.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
    }

    @Test
    @Tag("login")
    void loginFailIfPasswordIsIncorrect() {
        System.out.println("test4" + this);
        userService.add(IVAN);
        Optional<User> loggedIn = userService.login(IVAN.getUsername(), "dummy");
        assertThat(loggedIn).isEmpty();
    }

    @Test
    @Tag("login")
    void loginFailIfUserDoesNotExist() {
        System.out.println("test4" + this);
        userService.add(IVAN);
        Optional<User> loggedIn = userService.login("dummy", IVAN.getPassword());
        assertThat(loggedIn).isEmpty();
    }

    @Test
    @Tag("login")
    void throwExceptionIfUsernameOrPasswordIsNull() {
//    //old way of testing exceptions
//        try{
//            userService.login(null, "dummy");
//            fail();
//        } catch (Exception e) {
//            assertTrue(true);
//        }
        assertAll(
                () -> {
                    var e = assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy"));
                    assertThat(e.getMessage()).isEqualTo("Username or password should not be null");
                },
                () -> {
                    var e =assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null));
                    assertThat(e.getMessage()).isEqualTo("Username or password should not be null");
                }
        );
    }

    @Test
    void usersConvertedToMapById() {
        userService.add(IVAN, PETR);
        Map<Long, User> users = userService.getUserMapById();
        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                () -> assertThat(users).containsValues(IVAN, PETR)
        );


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
