package app.search.filters;

import app.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlbumFilter implements Filter<Song> {
    private String album;

    public AlbumFilter(final String album) {
        this.album = album;
    }

    /**
     * Checks if the song belongs to {@code album}
     * @param matchingObject The object to be matched
     * @return true, if it matches, false, otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        String songAlbum = matchingObject.getAlbum();

        return songAlbum.equals(album);
    }
}
