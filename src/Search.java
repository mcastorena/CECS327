import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Search {

    // TODO: Better search algo, Using linear search for now, just to get working

    // Params: The search string and a list of collections
    // Returns SearchResult which contains list of collections for songs and artists
    public static SearchResult search(String name, List<Collection> collection)
    {
        // Keep track of duplicate artists
        List<String> duplicateArtists = new ArrayList<>();

        List<Collection> searchSongList = new ArrayList<>();
        List<Collection> searchArtistList = new ArrayList<>();

        // Time the Search
        long start = System.nanoTime();
        for(int i = 0; i < collection.size(); i++)
        {
            Collection currentCollection = collection.get(i);
            String song = currentCollection.getSongTitle();
            String artist = currentCollection.getArtistName();

            // Add song to result list: 1) to the front of the list if it contains the exact phrase
            //                          2) to the end of the list if it contains the phrase
            if(song.equalsIgnoreCase(name))
            {
                searchSongList.add(0, currentCollection);
            }
            else if (Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(song).find())
            {
                searchSongList.add(currentCollection);
            }

            // Check if the artist is already in the results
            boolean artistInList = duplicateArtists.contains(artist);

            // Add artist to result list
            if(!artistInList) {
                if (artist.equals(name))
                {
                    currentCollection.setPrintFormat(false);
                    searchArtistList.add(0, currentCollection);
                }
                else if (Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(artist).find())
                {
                    currentCollection.setPrintFormat(false);
                    searchArtistList.add(currentCollection);
                }
                duplicateArtists.add(artist);
            }
        }
        long stop = System.nanoTime() - start;
        System.out.printf("SEARCH TIME: %.5f Seconds\n", (stop * (Math.pow(10,-9))));

        return new SearchResult(searchSongList, searchArtistList);
    }
}
