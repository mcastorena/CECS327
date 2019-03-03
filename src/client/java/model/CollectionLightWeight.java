package model;

public class CollectionLightWeight extends Collection {

// A container to hold all json objects related to the same releaseName id

    private static long serialVersionUID = 1L;

    private String artistName;
    private String songName;
    private String releaseName;
    private long idNum;

    public CollectionLightWeight(){}
    public CollectionLightWeight(long id, String songName, String artistName, String releaseName) {
        this.releaseName = releaseName;
        this.artistName = artistName;
        this.songName = songName;
        this.idNum = id;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getSongTitle() {
        return songName;
    }

    public long getId() {
        return idNum;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setId(long id) {
        this.idNum = id;
    }
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public Release getReleaseName() {
//        return releaseName;
//    }
//
//    public void setReleaseName(Release releaseName) {
//        this.releaseName = releaseName;
//    }
//
//    public Artist getArtistName() {
//        return artistName;
//    }
//
//    public void setArtistName(Artist artistName) {
//        this.artistName = artistName;
//    }
//
//    public Song getSongTitle() {
//        return songName;
//    }
//
//    public void setSongName(Song songName) {
//        this.songName = songName;
//    }
//
//    public String getSongTitle() {
//        return songName.getTitle();
//    }
//
//    public String getArtistName() {
//        return artistName.getName();
//    }
//
//    @Override
//    public String toString() {
//        return getArtistName();
//    }
//
//    public void serialize() {
//
//    }
}

