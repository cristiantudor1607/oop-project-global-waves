package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LyricsFilter implements Filter<Song>{
    private String pattern;

    public LyricsFilter(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(Song MatchingObject) {
        String songLyrics = MatchingObject.getLyrics();

        return songLyrics.contains(pattern);
    }
}
