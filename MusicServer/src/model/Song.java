package model;

import java.io.Serializable;

// Holds song info from json, this class is created by gson
public class Song implements Serializable {
    private float key;
    private float modeConfidence;
    private float artistMbtagsCount;
    private float keyConfidence;
    private float tatumsStart;
    private short year;
    private float duration;
    private double hotttnesss;
    private float beatsStart;
    private float timeSignatureConfidnce;
    private String title;
    private float barsConfidence;
    private String id;
    private float barsStart;
    private String artistMbTags;
    private float startOfFadeOut;
    private float tempo;
    private float endOfFadeIn;
    private float beatsConfidence;
    private float tatumsConfidence;
    private short mode;
    private float timeSignature;
    private float loudness;

    public float getKey() {
        return key;
    }

    public void setKey(float key) {
        this.key = key;
    }

    public float getModeConfidence() {
        return modeConfidence;
    }

    public void setModeConfidence(float modeConfidence) {
        this.modeConfidence = modeConfidence;
    }

    public float getArtistMbtagsCount() {
        return artistMbtagsCount;
    }

    public void setArtistMbtagsCount(float artistMbtagsCount) {
        this.artistMbtagsCount = artistMbtagsCount;
    }

    public float getKeyConfidence() {
        return keyConfidence;
    }

    public void setKeyConfidence(float keyConfidence) {
        this.keyConfidence = keyConfidence;
    }

    public float getTatumsStart() {
        return tatumsStart;
    }

    public void setTatumsStart(float tatumsStart) {
        this.tatumsStart = tatumsStart;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public double getHotttnesss() {
        return hotttnesss;
    }

    public void setHotttnesss(double hotttnesss) {
        this.hotttnesss = hotttnesss;
    }

    public float getBeatsStart() {
        return beatsStart;
    }

    public void setBeatsStart(float beatsStart) {
        this.beatsStart = beatsStart;
    }

    public float getTimeSignatureConfidnce() {
        return timeSignatureConfidnce;
    }

    public void setTimeSignatureConfidnce(float timeSignatureConfidnce) {
        this.timeSignatureConfidnce = timeSignatureConfidnce;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getBarsConfidence() {
        return barsConfidence;
    }

    public void setBarsConfidence(float barsConfidence) {
        this.barsConfidence = barsConfidence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getBarsStart() {
        return barsStart;
    }

    public void setBarsStart(float barsStart) {
        this.barsStart = barsStart;
    }

    public String getArtistMbTags() {
        return artistMbTags;
    }

    public void setArtistMbTags(String artistMbTags) {
        this.artistMbTags = artistMbTags;
    }

    public float getStartOfFadeOut() {
        return startOfFadeOut;
    }

    public void setStartOfFadeOut(float startOfFadeOut) {
        this.startOfFadeOut = startOfFadeOut;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public float getEndOfFadeIn() {
        return endOfFadeIn;
    }

    public void setEndOfFadeIn(float endOfFadeIn) {
        this.endOfFadeIn = endOfFadeIn;
    }

    public float getBeatsConfidence() {
        return beatsConfidence;
    }

    public void setBeatsConfidence(float beatsConfidence) {
        this.beatsConfidence = beatsConfidence;
    }

    public float getTatumsConfidence() {
        return tatumsConfidence;
    }

    public void setTatumsConfidence(float tatumsConfidence) {
        this.tatumsConfidence = tatumsConfidence;
    }

    public short isMode() {
        return mode;
    }

    public void setMode(short mode) {
        this.mode = mode;
    }

    public float getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(float timeSignature) {
        this.timeSignature = timeSignature;
    }

    public float getLoudness() {
        return loudness;
    }

    public void setLoudness(float loudness) {
        this.loudness = loudness;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                '}';
    }
}