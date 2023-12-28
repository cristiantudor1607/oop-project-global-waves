package app.users;

import app.player.entities.Album;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.pages.ArtistPage;
import app.pages.Page;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Artist extends User {
    private final List<Album> albums;
    private final ArtistPage selfPage;

    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        selfPage = new ArtistPage(this);
    }

    @Override
    public Page getPage() {
        return selfPage;
    }

    @Override
    public boolean hasAlbumWithName(String albumName) {
        for (Album a: albums) {
            if (a.getName().equals(albumName))
                return true;
        }

        return false;
    }

    @Override
    public Album getAlbumByName(String albumName) {
        for (Album album: albums)
            if (album.getName().equals(albumName))
                return album;

        return null;
    }

    @Override
    public boolean addAlbum(Album newAlbum) {
        return albums.add(newAlbum);
    }

    @Override
    public void removeAlbum(Album oldAlbum) {
        albums.remove(oldAlbum);
    }

    @Override
    public int getNumberOfLikes() {
        int sumOfLikes = 0;
        for (Album album: albums)
            sumOfLikes += album.getTotalLikesNumber();

        return sumOfLikes;
    }

    @Override
    public boolean hasEvent(String eventName) {
        for (Event e: selfPage.getEvents()) {
            if (e.getName().equals(eventName))
                return true;
        }

        return false;
    }

    @Override
    public Event getEvent(String eventName) {
        for (Event e : selfPage.getEvents()) {
            if (e.getName().equals(eventName))
                return e;
        }

        return null;
    }

    @Override
    public void addEvent(Event newEvent) {
        selfPage.getEvents().add(newEvent);
    }

    @Override
    public boolean removeEvent(Event event) {
        return selfPage.getEvents().remove(event);
    }

    @Override
    public boolean hasMerch(String merchName) {
        for (Merch m: selfPage.getMerchandising()) {
            if (m.getName().equals(merchName))
                return true;
        }

        return false;
    }

    @Override
    public void addMerch(Merch newMerch) {
        selfPage.getMerchandising().add(newMerch);
    }

    @Override
    public boolean isArtist() {
        return true;
    }

    @Override
    public boolean isNormalUser() {
        return false;
    }


}