package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.Album;

public class DescriptionFilter implements Filter<Album>{
    private final String descriptionPrefix;

    public DescriptionFilter(final String descriptionPrefix) {
        this.descriptionPrefix = descriptionPrefix;
    }

    @Override
    public boolean matches(Album matchingObject) {
        String albumDescription = matchingObject.getDescription();

        return albumDescription.equals(descriptionPrefix);
    }
}
