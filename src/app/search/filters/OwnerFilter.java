package app.search.filters;

import app.properties.OwnedEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OwnerFilter<T extends OwnedEntity> implements Filter<T> {
    private String owner;

    public OwnerFilter(final String owner) {
        this.owner = owner;
    }

    /**
     * Checks if the object's owner is {@code owner}
     * @param matchingObject The object to be matched
     * @return true, if the object has the same owner, false otherwise
     */
    @Override
    public boolean matches(final T matchingObject) {
        String objectOwner = matchingObject.getOwner();

        return objectOwner.equals(owner);
    }
}
