package server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import server.core.Server;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a method for splitting the music JSON into chunks, and a main method that verifies the contents
 * of the music JSON file.
 */
public class MusicJsonSplitter {

    /**
     * Returns a List object of 'chunks' of the music JSON file. Each chunk holds a percentage, determined by numChunks,
     * of all the data in the file (release, song, artist).
     *
     * @param filename  Name of the JSON file containing the music data
     * @param numChunks The number of chunks to split the file into.
     * @return A List object containing JsonArray chunks of the music JSON file
     * @throws IOException
     */
    public static List<JsonArray> getMusicJsonChunks(String filename, int numChunks) throws IOException {
        if (numChunks <= 0) {
            throw new NumberFormatException("Number of chunks must at least be 1.");
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray songArray = gson.fromJson(new FileReader(filename), JsonArray.class);

        ArrayList<JsonArray> songArrayChunks = new ArrayList<>(); // holds all the "tenths" of the music json
        JsonArray temp = null; // a "tenth" of the array

        // Split music.json
        for (int i = 0; i < songArray.size(); i++) {
            if (i % (int) (songArray.size() / numChunks) == 0) {
                temp = new JsonArray();
                songArrayChunks.add(temp);
            }

            if (temp != null) {
                temp.add(songArray.get(i));
            }
        }

        return songArrayChunks;
    }

    /**
     * Main method for verifying the contents of the music JSON file.
     *
     * @param args Arguments passed to main
     */
    public static void main(String[] args) {
        try {
            var arrays = getMusicJsonChunks(Server.class.getResource("/server/music.json").getPath(), 100);
            for (var v : arrays) {
                System.out.println(v.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
