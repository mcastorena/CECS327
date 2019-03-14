package server;

import server.core.SongDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SongDispatcherTest {
    private SongDispatcher dispatcher;

    @BeforeEach
    void setup() {
        dispatcher = new SongDispatcher();
    }

    @Test
    void getSongChunk() {
        try {
            String song = dispatcher.getSongChunk(41838L, 2L);

            System.out.println(song);

            assertNotNull(song);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSongFileSize() {
        Integer songSize = dispatcher.getFileSize(41838L);

        assertEquals(songSize, 7278929);
    }
}
