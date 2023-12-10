package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.properties.PlayableEntity;
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
     * Checks if the object has a name that start with this prefix
     * @param matchingObject the object that has the getName trait, to
     *                       be compared with this prefix
     * @return true, if the object name field starts with the this prefix, false
     * otherwise
     */
    @Override
    public boolean matches(final T matchingObject) {
        String objName = matchingObject.getName().toLowerCase();

        return objName.startsWith(prefix.toLowerCase());
    }
}
