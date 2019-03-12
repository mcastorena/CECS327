package client.gui.PlaylistList;

public class PlaylistListService {
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
