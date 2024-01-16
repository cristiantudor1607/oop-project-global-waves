package app.player.entities;

import app.management.IDContainer;
import app.users.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public final class Album extends Playlist {
    private final User artistLink;
    private final String artist;
    private final String description;
    private final boolean noDescription;
    private final int releaseYear;

    public static class Builder {
        private final String name;
        private final String artist;
        private final int creationTime;
        private List<Song> songs;
        private User artistLink;
        private String description;
        private int releaseYear;

        public Builder(final String name, final String artist, final int creationTime) {
            this.name = name;
            this.artist = artist;
            this.creationTime = creationTime;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder songs(final List<Song> songs) {
            this.songs = new ArrayList<>();
            this.songs.addAll(songs);
            return this;
        }

        public Builder releaseYear(final int releaseYear) {
            this.releaseYear = releaseYear;
            return this;
        }

        public Builder artistLink(final User artistLink) {
            this.artistLink = artistLink;
            return this;
        }

        public Album build() {
            return new Album(this);
        }

    }

    private Album(final Builder builder) {
        super(IDContainer.getInstance().useAlbumId());
        name = builder.name;
        creationTime = builder.creationTime;

        artist = builder.artist;
        description = builder.description;
        noDescription = description == null;
        releaseYear = builder.releaseYear;
        artistLink = builder.artistLink;
        setSongs(builder.songs);
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
     * Returns the identification number of the entity. It is usually an id
     * associated to the entity at creation.
     * @return An identification number bigger than {@code 0}, if the entity has
     * one, {@code 0} otherwise
     */
    @Override
    public int getIdentificationNumber() {
        return getId();
    }

    /**
     * Returns the identification number of the user that added the entity, if the
     * entity needs to be sorted by the time when user registered.
     * @return An identification number bigger than {@code 0}, if the entities needs to be
     * sorted by this criterion, {@code 0} otherwise. <b>For podcasts, it returns 0.</b>
     */
    @Override
    public int getCreatorIdForSorting() {
        return artistLink.getId();
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
     * Checks if the entity is an album
     * @return {@code true}, if it is an album, {@code false} otherwise
     */
    @Override
    public boolean isAlbum() {
        return true;
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
