package app.search.filters;

import app.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArtistFilter implements Filter<Song> {
    private String artist;

    public ArtistFilter(final String artist) {
        this.artist = artist;
    }

    /**
     * Checks if song belongs to {@code artist}
     * @param matchingObject The song to be matched
     * @return true, if it matches, false, otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        String songArtist = matchingObject.getArtist();
        return songArtist.equals(artist);
    }
}
