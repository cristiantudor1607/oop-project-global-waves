package globalwaves.player.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Artist extends User {
    private final List<Album> albums;

    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
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
    public boolean isArtist() {
        return true;
    }

    @Override
    public boolean isNormalUser() {
        return false;
    }


}
