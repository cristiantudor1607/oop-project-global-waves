package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlbumFilter implements Filter<Song> {
    private String album;

    public AlbumFilter(final String album) {
        this.album = album;
    }

    /**
     * Checks if the matchingObject has the same album as the filter album
     * @param matchingObject The Song to be compared with this album
     * @return true, if the matchingObject has the same album, false otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        String songAlbum = matchingObject.getAlbum();

        return songAlbum.equals(album);
    }
}
