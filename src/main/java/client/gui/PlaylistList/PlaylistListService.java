package client.gui.PlaylistList;

/**
 * TODO
 */
public class PlaylistListService {

    /**
     * Singleton instance of the PlaylistList service
     */
    private PlaylistListService instance;

    private PlaylistListService() {
    }

    public PlaylistListService getInstance() {
        if (instance == null) {
            return new PlaylistListService();
        }
        return instance;
    }
}
