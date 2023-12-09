package globalwaves.player.entities;

import fileio.input.SongInput;
import globalwaves.commands.enums.exitcodes.FollowExit;
import globalwaves.commands.enums.exitcodes.ShuffleExit;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Song extends AudioFile implements PlayableEntity {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private int releaseYear;
    private String artist;

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
        artist  =songToBeCopied.getArtist();
    }

    @Override
    public boolean canBeLiked() {
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
    public FollowExit.Code follow(String username) {
        return FollowExit.Code.NOT_A_PLAYLIST;
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
    public ShuffleExit.Code shuffle(int seed) {
       return ShuffleExit.Code.NOT_A_PLAYLIST;
    }

    @Override
    public ShuffleExit.Code unshuffle() {
        return ShuffleExit.Code.NOT_A_PLAYLIST;
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

}
