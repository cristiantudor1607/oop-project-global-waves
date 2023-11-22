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

    @Override
    public boolean matches(Song MatchingObject) {
        String songArtist = MatchingObject.getArtist();
        return songArtist.equals(artist);
    }
}
