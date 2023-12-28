package app.player.entities;

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
        songs.forEach(s -> {this.getSongs().add(s);});
    }

    public boolean isUsedInPlaylist() {
        for (Song s: getSongs()) {
            if (s.getPlaylistsInclusionCounter() > 0)
                return true;
        }

        return false;
    }

    @Override
    public String getPublicIdentity() {
        return artist;
    }

    @Override
    public boolean hasAudiofileFromUser(String username) {
        return artist.equals(username);
    }

    @Override
    public Album getCurrentAlbum() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album album = (Album) o;
        return artist.equals(album.artist) && getName().equals(album.getName());
    }
}
