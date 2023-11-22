package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenreFilter implements Filter<Song> {
    private String genre;

    public GenreFilter(final String genre) {
        this.genre = genre;
    }

    @Override
    public boolean matches(Song MatchingObject) {
        String songGenre = MatchingObject.getGenre().toLowerCase();
        return songGenre.equals(genre.toLowerCase());
    }
}
