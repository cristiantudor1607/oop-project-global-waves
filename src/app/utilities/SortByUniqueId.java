package app.utilities;

import app.properties.UniqueIdPossessor;

import java.util.Comparator;

public class SortByUniqueId implements Comparator<UniqueIdPossessor> {
    /**
     * Compares the ids of the entities. The result says what entity was created
     * first. It should be called only on entities of the same type.
     * @param o1 The first entity whose id will be compared
     * @param o2 The second entity whose id will be compared
     * @return A positive integer, if the first entity was created after the second
     * one, {@code 0}, if they were created at the same time, or a negative integer,
     * if the first entity was created before the second one. {@code 0} is a hypothetical
     * result rather than a real one, because 2 entities of the same type can't have the
     * same id.
     */
    @Override
    public int compare(final UniqueIdPossessor o1, final UniqueIdPossessor o2) {
        return o1.getIdentificationNumber() - o2.getIdentificationNumber();
    }
}
