package com.example.harmeet.linus.beans;

/**
 * Created by Ishmeet on 10/04/16.
 */
public class Song {
    String echonestId;
    String uri;
    String name;
    String thumbnail;
    String artist;

    public Song(String echonestId, String uri, String name, String thumbnail, String artist) {
        this.echonestId = echonestId;
        this.uri = uri;
        this.name = name;
        this.thumbnail = thumbnail;
        this.artist = artist;
    }

    public String getEchonestId() {
        return echonestId;
    }

    public void setEchonestId(String echonestId) {
        this.echonestId = echonestId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
