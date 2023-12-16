package app.search.filters;

import app.pages.Page;

public class ArtistPageFilter implements Filter<Page> {
    private final String artistPrefix;

    public ArtistPageFilter(final String artistPrefix) {
        this.artistPrefix = artistPrefix;
    }

    @Override
    public boolean matches(Page matchingObject) {
        String artistName = matchingObject.getArtistName();

        if (artistName == null)
            return false;

        return artistName.startsWith(artistPrefix);
    }
}
