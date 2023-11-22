package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.properties.OwnedEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OwnerFilter<T extends OwnedEntity> implements Filter<T>{
    private String owner;

    public OwnerFilter(final String owner) {
        this.owner = owner;
    }

    @Override
    public boolean matches(T MatchingObject) {
        String objectOwner = MatchingObject.getOwner();

        return objectOwner.equals(owner);
    }
}
