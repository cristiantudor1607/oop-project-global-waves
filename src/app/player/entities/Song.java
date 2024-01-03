package app.player.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fileio.input.SongInput;
import app.properties.PlayableEntity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter @Setter
public class Song extends AudioFile implements PlayableEntity, Comparable<Song> {
    private String album;
    private List<String> tags;
    private String lyrics;
    private String genre;
    private int releaseYear;
    private String artist;
    @JsonIgnore
    private int creationTime;
    @JsonIgnore
    private int likesNumber;
    @JsonIgnore
    private int playlistsInclusionCounter;

    @JsonCreator
    public Song(@JsonProperty("name") final String name,
                @JsonProperty("duration") final int duration,
                @JsonProperty("album") final String album,
                @JsonProperty("tags") final List<String> tags,
                @JsonProperty("lyrics") final String lyrics,
                @JsonProperty("genre") final String genre,
                @JsonProperty("releaseYear") final int releaseYear,
                @JsonProperty("artist") final String artist) {
        this.name = name;
        this.duration = duration;
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        creationTime = 0;
        likesNumber = 0;
        playlistsInclusionCounter = 0;
    }

    public Song(final SongInput input) {
        name = input.getName();
        duration = input.getDuration();
        album = input.getAlbum();

        /* Make a deep copy of the tags ArrayList */
        tags = new ArrayList<>();
        for (String inputTag: input.getTags()) {
            tags.add(new String(inputTag));
        }

        lyrics = input.getLyrics();
        genre = input.getGenre();
        releaseYear = input.getReleaseYear();
        artist = input.getArtist();
        creationTime = 0;
        likesNumber = 0;
    }

    /**
     * Increases likes number.
     */
    public void addLike() {
        likesNumber++;
    }

    /**
     * Decreases likes number.
     */
    public void removeLike() {
        likesNumber--;
    }

    /**
     * Increases the number of playlists where song is used.
     */
    public void increasePlaylistsCount() {
        playlistsInclusionCounter++;
    }

    /**
     * Decreases the number of playlists where song is used.
     */
    public void decreasePlaylistsCount() {
        playlistsInclusionCounter--;
    }

    /**
     * Returns the size of the entity. <b>Size of an entity is defined as number of audio files
     * encapsulated by the entity.</b>
     * @return The size of the entity
     */
    @Override
    public int getSize() {
        return 1;
    }

    /**
     * Returns the AudioFile at the specified index
     * @param index The index of the file
     * @return {@code null}, if the index is out of bounds, the {@code AudioFile} otherwise
     */
    @Override
    public AudioFile getAudioFileAtIndex(final int index) {
        if (index > 0) {
            return null;
        }

        return this;
    }

    /**
     * Returns the index of the file in the collection / entity.
     * @param file The file to be inspected.
     * @return The index of the file, or {@code -1 } if it doesn't contain the file
     */
    @Override
    public int getIndexOfFile(final AudioFile file) {
        return 0;
    }

    /**
     * Gets the first file in collection
     * @return The first file of the entity
     */
    @Override
    public AudioFile getFirstAudioFile() {
        return this;
    }

    /**
     * Returns the artist of the song.
     * @return The artist of the song.
     */
    @Override
    public String getPublicIdentity() {
        return artist;
    }

    /**
     * Returns the repeatValue formatted as String value
     * @param repeatValue The repeatValue to be converted
     * @return The corespondent string, or {@code null} if {@code repeatValue < 0} or
     * {@code repeatValue > 2}
     */
    @Override
    public String getRepeatStateName(final int repeatValue) {
        return switch (repeatValue) {
            case 0 -> "No Repeat";
            case 1 -> "Repeat Once";
            case 2 -> "Repeat Infinite";
            default -> null;
        };
    }

    /**
     * Checks if the entity is an empty file. Only a playlist can be empty,
     * if it doesn't have any song.
     * @return {@code true}, if it is an empty file, {@code false} otherwise
     */
    @Override
    public boolean isEmptyPlayableFile() {
        return false;
    }

    /**
     * Checks if the entity is a playlist
     * @return {@code true}, if it is a playlist or an album, {@code false} otherwise
     */
    @Override
    public boolean isPlaylist() {
        return false;
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
     * Checks if the entity needs to save its data to a history record
     * @return {@code true}, if it needs, {@code false} otherwise
     */
    @Override
    public boolean needsHistoryTrack() {
        return false;
    }

    /**
     * Checks if the forward and backward commands can be applied on the
     * entity
     * @return {@code true}, if they can be applied, {@code false} otherwise
     */
    @Override
    public boolean cantGoForwardOrBackward() {
        return true;
    }

    /**
     * If {@code this} is a playlist, returns its instance.
     * @return {@code this}, if it is a playlist, {@code null} otherwise
     */
    @Override
    public Playlist getCurrentPlaylist() {
        return null;
    }

    /**
     * If {@code this} is an album, returns its instance.
     * @return {@code this}, if it is an album, {@code null} otherwise
     */
    @Override
    public Album getCurrentAlbum() {
        return null;
    }

    /**
     * Checks if {@code this} is a Song
     * @return {@code true}, if it is a song, {@code false} otherwise
     */
    @Override
    public boolean isSong() {
        return true;
    }

    /**
     * If {@code this} is a song, return its instance.
     * @return {@code this}, if it is a song, {@code null} otherwise
     */
    @Override
    public Song getCurrentSong() {
        return this;
    }

    /**
     * If {@code this} is an episode, return its instance.
     * @return {@code this}, if it is an episode, {@code null} otherwise
     */
    @Override
    public Episode getCurrentEpisode() {
        return null;
    }

    /**
     * Compares this song with the specified object. The result is true if and only if
     * the argument is not null and is a Song object that represents the same song
     * as this object.
     *
     * @param o The object to compare this song against
     * @return {@code true}, if the given object represents the same song as this
     * song, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Song)) {
            return false;
        }
        Song song = (Song) o;
        return artist.equals(song.getArtist()) && name.equals(song.getName());
    }

    /**
     * Returns a hashcode value for this song. If two objects are equal according to the
     * equals method, then calling the hashCode method on each of the two objects must produce the
     * same integer result.
     *
     * @return A hashcode value for this song
     */
    @Override
    public int hashCode() {
        return Objects.hash(getArtist(), getName());
    }


    /**
     * Compares two songs by their name. The names ar compared lexicographically.
     * @param o The song to be compared.
     * @return {@code 0}, if the two songs have the same name, a value less than {@code 0},
     * if {@code this} name is lexicographically less than the o's name, and a value grater
     * than {@code 0}, if {@code this} name is lexicographically greater than the o's name
     */
    @Override
    public int compareTo(@NonNull final Song o) {
        return name.compareTo(o.name);
    }
}
