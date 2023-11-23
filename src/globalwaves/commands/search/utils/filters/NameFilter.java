package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * NameFilter description
 * @param <T>
 */
@Getter @Setter
public class NameFilter<T extends PlayableEntity> implements Filter <T>{
    private String prefix;

    public NameFilter (final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean matches(T MatchingObject) {
        String objName = MatchingObject.getName().toLowerCase();

        return objName.startsWith(prefix.toLowerCase());
    }
}
