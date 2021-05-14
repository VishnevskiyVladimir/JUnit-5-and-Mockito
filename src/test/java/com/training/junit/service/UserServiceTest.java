package com.training.junit.service;

import com.training.junit.TestBase;
import com.training.junit.dto.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("user")
@TestMethodOrder(MethodOrderer.Random.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class UserServiceTest extends TestBase {

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
    void usersEmptyIfNoUsersAdded(TestInfo testInfo) {
        System.out.println("test " + testInfo.getDisplayName()  + " userService: "+ this);
        var users = userService.getAll();
        assertThat(users).isEmpty();
    }

    @RepeatedTest(5)
    void usersSizeIfUserAdded(TestInfo testInfo) {
        System.out.println("test " + testInfo.getDisplayName()  + " userService: "+ this);
        userService.add(IVAN, PETR);
        var users = userService.getAll();
        assertThat(users).hasSize(2);
    }


//    @Nested
//    @DisplayName("test user login functionality")
//    @Tag("login")
//    class loginTest {
//
//        @Test
//        void loginFunctionalityPerformanceTest(TestInfo testInfo){
//            System.out.println("test " + testInfo.getDisplayName()  + " userService: "+ this);
//            assertTimeout(Duration.ofMillis(50L),() -> userService.login(IVAN.getUsername(), IVAN.getPassword()));
//        }
//
//        @Test
//        void loginSuccessIfUserExists(TestInfo testInfo) {
//            System.out.println("test " + testInfo.getDisplayName()  + " userService: "+ this);
//            userService.add(IVAN);
//            Optional<User> loggedIn = userService.login(IVAN.getUsername(), IVAN.getPassword());
//            assertThat(loggedIn).isPresent();
//            loggedIn.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
//        }
//
//        @Order(1)
//        @Test
//        void loginFailIfPasswordIsIncorrect(TestInfo testInfo) {
//            System.out.println("test " + testInfo.getDisplayName()  + " userService: "+ this);
//            userService.add(IVAN);
//            Optional<User> loggedIn = userService.login(IVAN.getUsername(), "dummy");
//            assertThat(loggedIn).isEmpty();
//        }
//
//        @Test
//        void loginFailIfUserDoesNotExist(TestInfo testInfo) {
//            System.out.println("test " + testInfo.getDisplayName()  + " userService: "+ this);
//            userService.add(IVAN);
//            Optional<User> loggedIn = userService.login("dummy", IVAN.getPassword());
//            assertThat(loggedIn).isEmpty();
//        }
//
//        @Test
//        void throwExceptionIfUsernameOrPasswordIsNull(TestInfo testInfo) {
////    //old way of testing exceptions
////        try{
////            userService.login(null, "dummy");
////            fail();
////        } catch (Exception e) {
////            assertTrue(true);
////        }
//            assertAll(
//                    () -> {
//                        var e = assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy"));
//                        assertThat(e.getMessage()).isEqualTo("Username or password should not be null");
//                    },
//                    () -> {
//                        var e = assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null));
//                        assertThat(e.getMessage()).isEqualTo("Username or password should not be null");
//                    }
//            );
//        }
//
//        @DisplayName("login param test")
//        @ParameterizedTest(name = "{arguments} test")
//        @MethodSource("com.training.junit.service.UserServiceTest#getArgumentsForLoginTest")
//        void loginParameterizedTest(String username, String password, Optional<User> user, TestInfo testInfo) {
//            System.out.println("test " + testInfo.getDisplayName()  + " userService: "+ this);
//            userService.add(IVAN, PETR);
//            var maybeUser = userService.login(username, password);
//            assertThat(maybeUser).isEqualTo(user);
//        }
//    }

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
    void usersConvertedToMapById(TestInfo testInfo) {
        System.out.println("test " + testInfo.getDisplayName()  + " userService: "+ this);
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
