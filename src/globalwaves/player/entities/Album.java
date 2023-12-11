package globalwaves.player.entities;

import lombok.Getter;

import java.util.List;

@Getter
public class Album extends Playlist {
    private final String artist;

    public Album(final String name, final String artist, final int creationTime) {
        super(name, artist, creationTime);
        this.artist = artist;
    }

    public Album(final String name, final String artist, final int creationTime,
                 final List<Song> songs) {
        this(name, artist, creationTime);

        for (Song s: songs) {
            this.getSongs().add(s);
        }
    }
}
