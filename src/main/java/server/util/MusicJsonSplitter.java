package server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import server.core.Server;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicJsonSplitter {
    /**
     * Returns a list of 'chunks' of the music Json file;
     * Each chunk holds a tenth of all the data in the file (release,
     * song, artist).
     *
     * @param filename
     * @param numChunks The number of chunks to split the file into.
     * @return
     * @throws IOException
     */
    public static List<JsonArray> getMusicJsonChunks(String filename, int numChunks) throws IOException {
        if (numChunks <= 0)
            throw new NumberFormatException("Number of chunks must at least be 1.");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray songArray = gson.fromJson(new FileReader(filename), JsonArray.class);

        ArrayList<JsonArray> songArrayChunks = new ArrayList<>(); // holds all the "tenths" of the music json
        JsonArray temp = null; // a "tenth" of the array
        // split music.json into tenths
        for (int i = 0; i < songArray.size(); i++) {
            // create a new "tenth" and add it to the list
            if (i % (int) (songArray.size() / numChunks) == 0) {
                temp = new JsonArray();
                songArrayChunks.add(temp);
            }

            temp.add(songArray.get(i));
        }
        return songArrayChunks;
    }

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
