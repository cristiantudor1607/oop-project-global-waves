package globalwaves.player.entities;

import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Song extends AudioFile {
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
    public boolean isSong() {
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
