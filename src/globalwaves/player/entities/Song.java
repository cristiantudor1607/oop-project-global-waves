package globalwaves.player.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fileio.input.SongInput;
import globalwaves.commands.enums.exitstats.stageone.FollowExit;
import globalwaves.commands.enums.exitstats.stageone.ShuffleExit;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
        this.creationTime = 0;
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
    public AudioFile getPlayableFile() {
        return this;
    }

    @Override
    public boolean needsHistoryTrack() {
        return false;
    }

    @Override
    public AudioFile getNextForPlaying(AudioFile currentFile, int repeatValue) {
        if (repeatValue == 0)
            return null;

        return this;
    }

    @Override
    public AudioFile getPrevForPlaying(AudioFile currentFile, int repeatValue) {
        if (repeatValue == 0)
            return null;

        return this;
    }

    @Override
    public FollowExit.Status follow(String username) {
        return FollowExit.Status.NOT_A_PLAYLIST;
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
    public ShuffleExit.Status shuffle(int seed) {
       return ShuffleExit.Status.NOT_A_PLAYLIST;
    }

    @Override
    public ShuffleExit.Status unshuffle() {
        return ShuffleExit.Status.NOT_A_PLAYLIST;
    }

    @Override
    public boolean cantGoForwardOrBackward() {
        return true;
    }

    @Override
    public String toString() {
        return "Song{" +
                "\nname='" + name + '\'' +
                "\nduration=" + duration +
                "\nalbum='" + album + '\'' +
                "\ntags=" + tags +
                "\nlyrics='" + lyrics + '\'' +
                "\ngenre='" + genre + '\'' +
                "\nreleaseYear=" + releaseYear +
                "\nartist='" + artist + '\'' +
                '}';
    }

    /**
     * @param o the song to be compared.
     * @return true, it the songs have the same name, false, otherwise
     */
    @Override
    public int compareTo(@NonNull Song o) {
        return name.compareTo(o.name);
    }
}
