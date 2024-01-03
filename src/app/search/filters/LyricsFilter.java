package app.search.filters;

import app.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LyricsFilter implements Filter<Song> {
    private String pattern;

    public LyricsFilter(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * Checks if the song lyrics contains the given string / pattern.
     * @param matchingObject The song to be matched
     * @return true, if lyrics contains {@code pattern}, false, otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        String songLyrics = matchingObject.getLyrics().toLowerCase();

        return songLyrics.contains(pattern.toLowerCase());
    }
}
