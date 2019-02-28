package model;

import java.io.Serializable;

// Holds release info from json, this class is created by gson
public class Release implements Serializable {
    private long id;
    private String name;

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

    @Override
    public String toString() {
        return "Release{" +
                "name='" + name + '\'' +
                '}';
    }
}