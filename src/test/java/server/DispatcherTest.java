package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.core.Dispatcher;
import server.core.LoginDispatcher;
import server.core.SongDispatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStreamReader;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DispatcherTest {
    private Dispatcher dispatcher;
    private static Gson gson;

    private static JsonArray jsonMusicObject;
    private static JsonObject jsonMethodsObject;

    @BeforeAll
    static void init() {
        gson = new Gson();

        jsonMusicObject = gson.fromJson(new InputStreamReader(
                DispatcherTest.class.getResourceAsStream("/music.json")),
                JsonArray.class);

        jsonMethodsObject = gson.fromJson(new InputStreamReader(
                DispatcherTest.class.getResourceAsStream("/methods.json")),
                JsonObject.class);
    }

    @BeforeEach
    void setup() {
        dispatcher = new Dispatcher();
    }

    @Test
    void registerDispatcherService() {
        dispatcher.registerDispatcher(SongDispatcher.class.getSimpleName(), new SongDispatcher());

        assertFalse(dispatcher.getDispatchers().isEmpty());
    }

    @Test
    void stringifyJsonToArray_login() {
        JsonObject loginMethodJson = jsonMethodsObject.get("login").getAsJsonObject();
        JsonObject paramsObject = loginMethodJson.get("params").getAsJsonObject();
        paramsObject.addProperty("username", "user");
        paramsObject.addProperty("password", "pass");

        String[] params = dispatcher.stringifyParametersToArray(loginMethodJson);

        String[] expected = { "user", "pass" };

        assertArrayEquals(params, expected);
    }

    @ParameterizedTest
    @MethodSource("getSongChunkProvider")
    void stringifyJsonToArray_song(JsonObject songChunkMethodJson) {

        String[] params = dispatcher.stringifyParametersToArray(songChunkMethodJson);

        String[] expected = { "41838", "2" };
        assertArrayEquals(params, expected);
    }

    @ParameterizedTest
    @MethodSource("getSongChunkProvider")
    void typifyParameters_getSongChunk(JsonObject songChunkMethodJson) {
        Class[] types = { Long.class, Integer.class };

        songChunkMethodJson = jsonMethodsObject.get("getSongChunk").getAsJsonObject();
        String[] params = dispatcher.stringifyParametersToArray(songChunkMethodJson);

        Object[] typedParams = dispatcher.typifyParameters(params, types);

        Object[] expected = { 41838L, 2 };
        assertArrayEquals(typedParams, expected);
    }

    @ParameterizedTest
    @MethodSource("getSongChunkProvider")
    void songChunkDispatch_notError(JsonObject songChunkMethodJson) {

        dispatcher.registerDispatcher(LoginDispatcher.class.getSimpleName(), new LoginDispatcher());
        dispatcher.registerDispatcher(SongDispatcher.class.getSimpleName(), new SongDispatcher());

        String response = dispatcher.dispatch(gson.toJson(songChunkMethodJson));

        assertFalse(response.contains("error"), "Error response object was received.");
    }

    private static Stream<Arguments> getSongChunkProvider() {
        JsonObject songChunkMethodJson = jsonMethodsObject.get("getSongChunk").getAsJsonObject();
        JsonObject paramsObject = songChunkMethodJson.get("params").getAsJsonObject();
        paramsObject.addProperty("song", "41838");
        paramsObject.addProperty("fragment", "2");

        return Stream.of(
            Arguments.of(songChunkMethodJson)
        );
    }
}
