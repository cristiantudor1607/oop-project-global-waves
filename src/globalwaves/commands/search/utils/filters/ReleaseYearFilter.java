package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReleaseYearFilter implements Filter<Song> {
    private Integer releaseYear;
    private boolean greater;

    public ReleaseYearFilter(String inputReleaseYear) {
        if (inputReleaseYear.startsWith(">"))
            greater = true;
        else
            greater = false;

        String removedSign = inputReleaseYear.substring(1);
        releaseYear = Integer.parseInt(removedSign);
    }
    @Override
    public boolean matches(Song MatchingObject) {
        int songReleaseYear = MatchingObject.getReleaseYear();

        return (greater && songReleaseYear > releaseYear) ||
                (!greater && songReleaseYear < releaseYear);
    }
}
