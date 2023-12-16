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
     * Checks if the matchingObject lyrics contains this pattern
     * @param matchingObject The song to be compared with this pattern
     * @return true, if the song lyrics contains the pattern String, false otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        String songLyrics = matchingObject.getLyrics().toLowerCase();

        return songLyrics.contains(pattern.toLowerCase());
    }
}
