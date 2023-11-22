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

    @Override
    public boolean matches(Song MatchingObject) {
        String songAlbum = MatchingObject.getAlbum();

        return songAlbum.equals(album);
    }
}
