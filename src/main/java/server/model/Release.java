package server.model;

import java.io.Serializable;

/**
 * This class represents the release info of a Song; created by GSON
 */
public class Release implements Serializable {

    /**
     * ID of the release
     */
    private long id;

    /**
     * Name of the release
     */
    private String name;

    //region Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion

    @Override
    public String toString() {
        return "Release{" +
                "name='" + name + '\'' +
                '}';
    }
}