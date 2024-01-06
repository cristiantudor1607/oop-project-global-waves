package app.properties;

@FunctionalInterface
public interface UniqueIdPossessor {
    /**
     * Returns the identification number of the entity. It is usually an id
     * associated to the entity at creation.
     * @return An identification number bigger than {@code 0}, if the entity has
     * one, {@code 0} otherwise
     */
    int getIdentificationNumber();
}
