package app.player.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fileio.input.SongInput;
import app.exitstats.stageone.ShuffleExit;
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

    /**
     * "Converter" Constructor: it creates a Song object based on a SongInput
     * object
     * @param input The SongInput object to be converted
     */
    public Song(SongInput input) {
        name = input.getName();
        duration = input.getDuration();
        album = input.getAlbum();

        /* Make a deep copy of the tags ArrayList */
        tags = new ArrayList<>();
        for (String inputTag: input.getTags()) {
            tags.add(inputTag);
        }

        lyrics = input.getLyrics();
        genre = input.getGenre();
        releaseYear = input.getReleaseYear();
        artist = input.getArtist();
        creationTime = 0;
        likesNumber = 0;
    }

    /**
     * Copy Constructor
     * @param songToBeCopied The song to be copied
     */
    public Song(Song songToBeCopied) {
        name = songToBeCopied.getName();
        duration = songToBeCopied.getDuration();
        album = songToBeCopied.getAlbum();
        tags = new ArrayList<>();
        for (String s : songToBeCopied.getTags())
            tags.add(s);

        lyrics = songToBeCopied.getLyrics();
        genre = songToBeCopied.getGenre();
        releaseYear = songToBeCopied.getReleaseYear();
        artist = songToBeCopied.getArtist();
        creationTime = songToBeCopied.creationTime;
        likesNumber = 0;
    }

    @Override
    public int getIndexOfFile(AudioFile file) {
        return 0;
    }

    @Override
    public AudioFile getAudioFileAtIndex(int index) {
        if (index > 0)
            return null;

        return this;
    }

    @Override
    public int getSize() {
        return 1;
    }

    public List<String> getFollowers() {
        return null;
    }

    public void addLike() {
        likesNumber++;
    }

    public void removeLike() {
        likesNumber--;
    }

    public void increasePlaylistsCount() {
        playlistsInclusionCounter++;
    }

    public void decreasePlaylistsCount() {
        playlistsInclusionCounter--;
    }

    @Override
    public boolean isSong() {
        return true;
    }

    @Override
    public boolean isEmptyPlayableFile() {
        return false;
    }

    @Override
    public AudioFile getFirstAudioFile() {
        return this;
    }

    @Override
    public boolean needsHistoryTrack() {
        return false;
    }

    @Override
    public String getRepeatStateName(int repeatValue) {
        switch (repeatValue) {
            case 0 -> {
                return "No Repeat";
            }
            case 1 -> {
                return "Repeat Once";
            }
            case 2 -> {
                return "Repeat Infinite";
            }
        }

        return null;
    }

    @Override
    public boolean cantGoForwardOrBackward() {
        return true;
    }

    @Override
    public Playlist getCurrentPlaylist() {
        return null;
    }

    @Override
    public String getPublicIdentity() {
        return artist;
    }

    @Override
    public Song getCurrentSong() {
        return this;
    }

    @Override
    public Episode getCurrentEpisode() {
        return null;
    }

    @Override
    public boolean hasAudiofileFromUser(String username) {
        return artist.equals(username);
    }

    @Override
    public Album getCurrentAlbum() {
        return null;
    }

    @Override
    public boolean isPlaylist() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return artist.equals(song.getArtist()) && name.equals(song.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArtist());
    }

    @Override
    public int compareTo(@NonNull Song o) {
        return name.compareTo(o.name);
    }
}
