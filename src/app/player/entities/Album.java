package app.player.entities;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class Album extends Playlist {
    private final String artist;
    private final String description;
    private boolean noDescription;
    private final int releaseYear;

    public Album(final String artist, final String name, final String description,
                 final int releaseYear, final int creationTime) {
        super(artist, name, creationTime);
        this.description = description;
        noDescription = false;
        this.artist = artist;
        this.releaseYear = releaseYear;
    }

    public Album(final String artist, final String name, final String description,
                 final int releaseYear, final int creationTime, final List<Song> songs) {
        // Create the album without songs
        this(artist, name, description, releaseYear, creationTime);

        // Add the songs
        songs.forEach(s -> this.getSongs().add(s));
    }

    /**
     * Checks if at least one song from album is used in a playlist.
     * @return {@code true}, if at least one song is used, {@code false} otherwise
     */
    public boolean isUsedInPlaylist() {
        for (Song s: getSongs()) {
            if (s.getPlaylistsInclusionCounter() > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the username of the artist that owns this.
     * @return The username of the artist
     */
    @Override
    public String getPublicIdentity() {
        return artist;
    }

    /**
     * Checks if the entity contains at least one audio file owned by the user
     * @param username The username of the user
     * @return {@code true}, if it contains, {@code false} otherwise
     */
    @Override
    public boolean hasAudiofileFromUser(final String username) {
        return artist.equals(username);
    }

    /**
     * If {@code this} is an album, returns its instance.
     * @return {@code this}, if it is an album, {@code null} otherwise
     */
    @Override
    public Album getCurrentAlbum() {
        return this;
    }

    /**
     * Compares this album with the specified object. The result is true if and only if
     * the argument is not null and is an Album object that represents the same album
     * as this object.
     *
     * @param o The object to compare this album against
     * @return {@code true}, if the given object represents the same album as this
     * song, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Album)) {
            return false;
        }
        Album album = (Album) o;
        return artist.equals(album.artist) && getName().equals(album.getName());
    }

    /**
     * Returns a hashcode value for this album. If two objects are equal according to the
     * equals method, then calling the hashCode method on each of the two objects must produce the
     * same integer result.
     *
     * @return A hashcode value for this album
     */
    @Override
    public int hashCode() {
        return Objects.hash(getArtist(), getName());
    }
}
