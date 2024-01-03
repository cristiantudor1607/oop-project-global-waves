package app.search.filters;

import app.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * NameFilter description
 * @param <T>
 */
@Getter @Setter
public class NameFilter<T extends PlayableEntity> implements Filter<T> {
    private String prefix;

    public NameFilter(final String prefix) {
        this.prefix = prefix;
    }

    /**
     * Checks if the object's name starts with {@code prefix}
     * @param matchingObject The object to be matched
     * @return true, if the object's name starts with {@code prefix}, false
     * otherwise
     */
    @Override
    public boolean matches(final T matchingObject) {
        String objName = matchingObject.getName().toLowerCase();

        return objName.startsWith(prefix.toLowerCase());
    }
}
