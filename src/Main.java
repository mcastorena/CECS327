import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static MusicPlayer musicPlayer;

    public static void main(String[] args) {

        //region Process JSON file
        Gson gson = new Gson();
        List<Collection> collection = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("music.json"));
            JsonReader jsonReader = new JsonReader(br);

            // Clear the first "[" in the json file
            jsonReader.beginArray();

            // Parse the json file
            while (jsonReader.hasNext()) {
                Release release;
                Artist artist;
                Song song;

                // If the artist has already been parsed, don't add new object.
                boolean oldArtist = false;

                // Clear the first "{" for each json object
                jsonReader.beginObject();

                jsonReader.nextName();
                release = gson.fromJson(jsonReader, Release.class);

                jsonReader.nextName();
                artist = gson.fromJson(jsonReader, Artist.class);

                jsonReader.nextName();
                song = gson.fromJson(jsonReader, Song.class);

                jsonReader.endObject();

                // Check if artist was previously parsed
                for (int i = 0; i < collection.size(); i++) {
                    Artist artistInCollection = collection.get(i).getArtist();
                    if (artistInCollection.getName().equals(artist.getName())) {
                        oldArtist = true;
                        collection.add(new Collection(release, artistInCollection, song));
                        break;
                    }
                }
                if (!oldArtist) {
                    collection.add(new Collection(release, artist, song));
                }
            }
            jsonReader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion JSON Processing

        musicPlayer = MusicPlayer.getInstance();
        musicPlayer.setCollection(collection);

        musicPlayer.pack();
        musicPlayer.setVisible(true);
    }
}