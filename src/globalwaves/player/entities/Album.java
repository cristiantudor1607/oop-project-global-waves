package globalwaves.player.entities;

import lombok.Getter;

@Getter
public class Album extends Playlist {
    private final String artist;

    public Album(final String name, final String artist, final int creationTime) {
        super(name, artist, creationTime);
        this.artist = artist;
    }
}
