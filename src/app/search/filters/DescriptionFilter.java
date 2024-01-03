package app.search.filters;

import app.player.entities.Album;

public class DescriptionFilter implements Filter<Album> {
    private final String descriptionPrefix;

    public DescriptionFilter(final String descriptionPrefix) {
        this.descriptionPrefix = descriptionPrefix;
    }

    /**
     * Checks if the album description starts with {@code descriptionPrefix}
     * @param matchingObject The object to be matched
     * @return true, if it matches, false, otherwise
     */
    @Override
    public boolean matches(final Album matchingObject) {
        String albumDescription = matchingObject.getDescription();

        return albumDescription.startsWith(descriptionPrefix);
    }
}
