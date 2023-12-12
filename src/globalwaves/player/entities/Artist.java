package globalwaves.player.entities;

import globalwaves.player.entities.paging.ArtistPage;
import globalwaves.player.entities.paging.Page;
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
    public boolean addAlbum(Album newAlbum) {
        return albums.add(newAlbum);
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
    public void addEvent(Event newEvent) {
        selfPage.getEvents().add(newEvent);
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
