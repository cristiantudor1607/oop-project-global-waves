package app.search.filters;

import app.pages.Page;

public class ArtistPageFilter implements Filter<Page> {
    private final String artistPrefix;

    public ArtistPageFilter(final String artistPrefix) {
        this.artistPrefix = artistPrefix;
    }

    /**
     * Checks if the page artist name starts with {@code artistPrefix}.
     * @param matchingObject The object to be matched
     * @return true, if it matches, false otherwise
     */
    @Override
    public boolean matches(final Page matchingObject) {
        String artistName = matchingObject.getArtistName();


        return artistName != null && artistName.startsWith(artistPrefix);
    }
}
