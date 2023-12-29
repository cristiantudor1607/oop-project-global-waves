package app.properties;

public interface OwnedEntity {
    /**
     * Returns the owner of the entity. The class that implements the interface
     * has to have the {@code owner} field, or some other field that acts like an owner.
     * @return The name of the owner
     */
    String getOwner();
}
