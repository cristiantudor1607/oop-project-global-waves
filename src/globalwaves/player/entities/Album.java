package globalwaves.player.entities;

import lombok.Getter;

import java.util.List;

@Getter
public class Album extends Playlist {
    private final String artist;

    public Album(final String artist, final String name,final int creationTime) {
        super(artist, name, creationTime);
        this.artist = artist;
    }

    public Album(final String artist, final String name,final int creationTime,
                 final List<Song> songs) {
        this(artist, name, creationTime);

        for (Song s: songs) {
            this.getSongs().add(s);
        }
    }
}
