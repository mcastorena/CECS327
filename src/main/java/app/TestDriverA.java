package app;

import model.*;
import utility.Deserializer;
import utility.Search;
import utility.Serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestDriverA {
    public static void main(String[] args) {
        try {
        // 1. Deserialize Music and User JSONs into POJOs.
            Deserializer deserializer = new Deserializer();
            List<User> users = deserializer.deserializeUsers();

            /**
             *  Map used for login.
             *  Key: username+password, no space
             *  Value: the User object
             */
            HashMap<String,User> usersInfo = new HashMap<>();

            for (User u : users) {
                if (usersInfo.containsValue(u)) {
                    throw new IllegalStateException("Duplicate user found in usersInfo");
                }
                usersInfo.put(u.getUsername()+u.getPassword(), u);
            }
        // end 1.
        ///////////////////////////////////////////////////////////////
        // 2. Login using a makeshift login system
            String username = "mark",
                    password = "manson";

            User currentSession = null;
            if ((currentSession = usersInfo.get(username+password)) == null) {
                System.out.println("Login failed.");
                return;
            }
            System.out.println("Login succeeded.");
        // end 2.
        ///////////////////////////////////////////////////////////////
        // 3. Create a song and add it to the user's "hits" Playlist
            Release r = new Release();
            r.setId(555);
            r.setName("A new song");

            Artist a = new Artist();
            a.setName("John");

            Song s = new Song();
            s.setTitle("A new songs");

            Collection c = new Collection(r, a, s);
            currentSession.getUserProfile().addSongToPlaylist(c, "hits");
        // end 3.
        ///////////////////////////////////////////////////////////////
        // 4. Add a new playlist, then a song, to the user's profile
            Playlist newPlaylist = new Playlist("New Songs");

            currentSession.getUserProfile().addPlaylist(newPlaylist.getName(), newPlaylist);
            currentSession.getUserProfile().addSongToPlaylist(c, newPlaylist.getName());
//            currentSession.getUserProfile().removePlaylist(newPlaylist.getName());
        // end 4.
        ///////////////////////////////////////////////////////////////
        // 5. Remove a playlist
            Playlist removeIt = new Playlist("removeThis");

            currentSession.getUserProfile().addPlaylist(removeIt.getName(), removeIt);
            currentSession.getUserProfile().removePlaylist(removeIt.getName());
        // end 5.
        ///////////////////////////////////////////////////////////////
        // 6. Search for a song
            // Songs in the Music JSON
            List<Collection> allSongs = deserializer.getMusicDatabase();

            // Songs in our ("the user") music directory
            List<Collection> ownedSongs = new ArrayList(deserializer.getUserLibrary().values());

            // Search for song titles for 'mean' in OWNED music and display it
            SearchResult result = Search.search("mean", ownedSongs);

            System.out.printf("%20s %70s\n\n", "Song", "Artist");
            for(int i = 0; i < result.getSongResultSize(); i++)
            {
                System.out.printf("%-80s %-80s\n", result.getSongTitle(i), result.getArtistForSong(i));
            }

            // Search by song ID for internal implementation
            Collection cResult = Search.searchByID(300822, allSongs);
            System.out.println("\n\nResult for id:300822: " + cResult.getSongTitle());
        // end 6.
        ///////////////////////////////////////////////////////////////
        // 7. play a song with the mp3 player

            // re-comment to use
//            mp3Player myPlayer = mp3Player.getInstance();
//            myPlayer.pack();
//            myPlayer.setVisible(true);

            /**
             * Notes:
             *  - mp3s are located in the 'music' folder
             *  - mp3s must be named by their release ID for deserializing
             *  - mp3 file names stored in the Deserializer do not include the extension,
             *      so append '.mp3' at the end to play songs
             */
//            myPlayer.changeSong("./music/" + 300822 + ".mp3");
        // end 7.
        ///////////////////////////////////////////////////////////////
        // 8. Update the User JSON files at (maybe the end of the program)

            // Rewrites the entire file using a List of Users
            new Serializer().updateUsersJson(users);
        // end 8.
        ///////////////////////////////////////////////////////////////

            System.out.println();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
