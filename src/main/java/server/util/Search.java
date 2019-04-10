package server.util;

import server.model.Collection;
import server.model.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * TODO:
 */
public class Search {

    // TODO: Better search algo, Using linear search for now, just to get working

    /**
     * Searches for a song through a List of Collection objects
     *
     * @param name       Name of a song being searched for
     * @param collection List of Collection objects
     * @return A SearchResult that contains a list of Collections for songs and artists
     */
    public static SearchResult search(String name, List<Collection> collection) {
        // Keep track of duplicate artists
        List<String> duplicateArtists = new ArrayList<>();
        List<Collection> searchSongList = new ArrayList<>();
        List<Collection> searchArtistList = new ArrayList<>();

        long start; // Start time
        long stop;  // Stop time

        // Time the Search
        start = System.nanoTime();

        // Begin search
        for (Collection currentCollection : collection) {
            String song = currentCollection.getSongTitle();
            String artist = currentCollection.getArtistName();

            // Add song to result list: 1) to the front of the list if it contains the exact phrase
            //                          2) to the end of the list if it contains the phrase
            if (song.equalsIgnoreCase(name)) {
                searchSongList.add(0, currentCollection);
            } else if (Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(song).find()) {
                searchSongList.add(currentCollection);
            }

            // Check if the artist is already in the results
            boolean artistInList = duplicateArtists.contains(artist);

            // Add artist to result list
            if (!artistInList) {
                if (artist.equals(name)) {
                    searchArtistList.add(0, currentCollection);
                } else if (Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(artist).find()) {
                    searchArtistList.add(currentCollection);
                }
                duplicateArtists.add(artist);
            }
        }
        stop = System.nanoTime() - start;
        System.out.printf("SEARCH TIME: %.5f Seconds\n", (stop * (Math.pow(10, -9))));

        return new SearchResult(searchSongList, searchArtistList);
    }

    /**
     * Searches for a Song by it's ID using linear search
     *
     * @param id         Song's ID
     * @param collection List of Collection objects
     * @return If found, a Collection object representing the Song - otherwise null
     */
    // TODO: sort the collection by id in app initialization
    // TODO: Then change this to binary search.
    public static Collection searchByID(long id, List<Collection> collection) {
        for (Collection c : collection) {
            if (c.getId() == id) {
                return c;
            }
        }

        return null;
    }
}
