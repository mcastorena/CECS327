package server;

import server.core.PlaylistDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlaylistDispatcherTest {
    PlaylistDispatcher playlistDispatcher;
    @BeforeEach
    void setup() {
        playlistDispatcher = new PlaylistDispatcher();
    }

    @Test
    void getPlaylistsSize() {
        playlistDispatcher.getPlaylistsSize(1);
    }
}
