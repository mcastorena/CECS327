package model;

import java.io.Serializable;

// Holds artist info from json, this class is created by gson
public class Artist implements Serializable {
    private float termsFreq;
    private String terms;
    private String name;
    private double familiarity;
    private double longitude;
    private String id;
    private String location;
    private double latitude;
    private String similar;
    private double hotttnesss;

    public float getTermsFreq() {
        return termsFreq;
    }

    public void setTermsFreq(float termsFreq) {
        this.termsFreq = termsFreq;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(double familiarity) {
        this.familiarity = familiarity;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getSimilar() {
        return similar;
    }

    public void setSimilar(String similar) {
        this.similar = similar;
    }

    public double getHotttness() {
        return hotttnesss;
    }

    public void setHotttness(double hotttness) {
        this.hotttnesss = hotttness;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                '}';
    }
}