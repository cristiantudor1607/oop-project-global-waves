package app.search.filters;

import app.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenreFilter implements Filter<Song> {
    private String genre;

    public GenreFilter(final String genre) {
        this.genre = genre;
    }

    /**
     * Checks if the song has {@code genre}
     * @param matchingObject The song to be matched
     * @return true, if the song has the same genre, false otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        String songGenre = matchingObject.getGenre().toLowerCase();
        return songGenre.equals(genre.toLowerCase());
    }
}
