package server;

import server.core.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ServerTest {
    @Test
    void testServer() {
        Server server = new Server();
        assertNotNull(server);
    }
}
