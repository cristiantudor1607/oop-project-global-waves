package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReleaseYearFilter implements Filter<Song> {
    private Integer releaseYear;
    private boolean greater;

    public ReleaseYearFilter(final String inputReleaseYear) {
        greater = inputReleaseYear.startsWith(">");

        String removedSign = inputReleaseYear.substring(1);
        releaseYear = Integer.parseInt(removedSign);
    }

    /**
     * Checks if the song release year is greater / smaller that this releaseYear
     * @param matchingObject The song to be compared with this releaseYear
     * @return true, if the song year should be greater and it is, or the song year
     * should be smaller and it is, false otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        int songReleaseYear = matchingObject.getReleaseYear();

        return (greater && songReleaseYear > releaseYear)
                || (!greater && songReleaseYear < releaseYear);
    }
}
