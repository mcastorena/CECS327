package server;

import server.core.LoginDispatcher;
import server.core.Server;
import server.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LoginDispatcherTest {
    private LoginDispatcher dispatcher;

    @BeforeEach
    void setup() {
        dispatcher = new LoginDispatcher();
    }

    @Test
    void login_valid() {
        Server.usersInfo.put("userpass", new User("user", "", "pass"));
        String loginToken = null;

        try {
            loginToken = dispatcher.login("user", "pass");
            System.out.println(loginToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(loginToken);
    }

    @Test
    void login_invalid() {
        String loginToken = null;
        try {
            loginToken = dispatcher.login("user", "pass");
            System.out.println(loginToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNull(loginToken);
    }
}
