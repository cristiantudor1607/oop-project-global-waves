package globalwaves.player.entities;

import lombok.Getter;

import java.util.List;

@Getter
public class Album extends Playlist {
    private final String artist;
    private final String description;

    public Album(final String artist, final String name, final String description,
                 final int creationTime) {
        super(artist, name, creationTime);
        this.description = description;
        this.artist = artist;
    }

    public Album(final String artist, final String name, final String description,
                 final int creationTime, final List<Song> songs) {
        // Create the album without songs
        this(artist, name, description, creationTime);

        // Add the songs
        for (Song s: songs) {
            this.getSongs().add(s);
        }

        // Recompute the playing order
        List<Integer> order = getNumericalOrder();
        this.setPlayOrder(order);
    }

    @Override
    public String getPublicPerson() {
        return artist;
    }

    @Override
    public boolean hasAudiofileFromUser(String username) {
        return artist.equals(username);
    }

    @Override
    public Album getWorkinOnAlbum() {
        return this;
    }
}
