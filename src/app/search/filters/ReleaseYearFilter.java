package app.search.filters;

import app.player.entities.Song;
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
     * Checks if the song's releaseYear is either smaller than {@code releaseYear}, or
     * bigger than {@code releaseYear}
     * @param matchingObject The song to be matched
     * @return true, either the releaseYear should be greater, and it is, or it should
     * be smaller and it is, false, otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        int songReleaseYear = matchingObject.getReleaseYear();

        return (greater && songReleaseYear > releaseYear)
                || (!greater && songReleaseYear < releaseYear);
    }
}
