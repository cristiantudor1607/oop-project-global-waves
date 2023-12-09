package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArtistFilter implements Filter<Song> {
    private String artist;

    public ArtistFilter(final String artist) {
        this.artist = artist;
    }

    /**
     * Checks if the matchingObject  has the same artist as the filter artist
     * @param matchingObject The song to be compared with this artist
     * @return true, if the song has the same artist, false otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        String songArtist = matchingObject.getArtist();
        return songArtist.equals(artist);
    }
}
