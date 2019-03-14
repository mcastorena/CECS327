package server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.util.Deserializer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeserializerTest {
    private static Deserializer deserializer;

    @BeforeAll
    static void setup() {
        deserializer = new Deserializer();
    }

    @Test
    void musicFilePathExists() {
        System.out.println(Deserializer.MUSIC_STREAM.toString());
        assertNotNull(Deserializer.MUSIC_STREAM);
    }

    @Test
    void loadOwnedMusicIds() {
        System.out.println(deserializer.getOwnedIDs());
        assertNotNull(deserializer.getOwnedIDs());
    }

    @Test
    void getMusicDatabase_notNull() {
        System.out.println(deserializer.getMusicDatabase());
        assertNotNull(deserializer.getMusicDatabase());
    }

    @Test
    void deserializePlaylists() {

    }
}
