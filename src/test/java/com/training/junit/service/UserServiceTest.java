package com.training.junit.service;

import com.training.junit.dto.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
@Tag("user")
@TestMethodOrder(MethodOrderer.Random.class)
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

    @RepeatedTest(5)
    void usersSizeIfUserAdded() {
        System.out.println("test2" + this);
        userService.add(IVAN, PETR);
        var users = userService.getAll();
        assertThat(users).hasSize(2);
    }


    @Nested
    @DisplayName("test user login functionality")
    @Tag("login")
    class loginTest {
        @Test
        void loginSucessIfUserExists() {
            System.out.println("test3" + this);
            userService.add(IVAN);
            Optional<User> loggedIn = userService.login(IVAN.getUsername(), IVAN.getPassword());
            assertThat(loggedIn).isPresent();
            loggedIn.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
        }

        @Order(1)
        @Test
        void loginFailIfPasswordIsIncorrect() {
            System.out.println("test4" + this);
            userService.add(IVAN);
            Optional<User> loggedIn = userService.login(IVAN.getUsername(), "dummy");
            assertThat(loggedIn).isEmpty();
        }

        @Test
        void loginFailIfUserDoesNotExist() {
            System.out.println("test4" + this);
            userService.add(IVAN);
            Optional<User> loggedIn = userService.login("dummy", IVAN.getPassword());
            assertThat(loggedIn).isEmpty();
        }

        @Test
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
                        var e = assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null));
                        assertThat(e.getMessage()).isEqualTo("Username or password should not be null");
                    }
            );
        }

        @DisplayName("login param test")
        @ParameterizedTest(name = "{arguments} test")
        @MethodSource("com.training.junit.service.UserServiceTest#getArgumentsForLoginTest")
        void loginParameterizedTest(String username, String password, Optional<User> user) {
            userService.add(IVAN, PETR);
            var maybeUser = userService.login(username, password);
            assertThat(maybeUser).isEqualTo(user);
        }
    }

    static Stream<Arguments> getArgumentsForLoginTest() {
        return Stream.of(
                Arguments.of(IVAN.getUsername(), IVAN.getPassword(), Optional.of(IVAN)),
                Arguments.of(PETR.getUsername(), PETR.getPassword(), Optional.of(PETR)),
                Arguments.of(PETR.getUsername(), "dummy", Optional.empty()),
                Arguments.of("dummy", PETR.getPassword(), Optional.empty()),
                Arguments.of("dummy", "dummy", Optional.empty())
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
