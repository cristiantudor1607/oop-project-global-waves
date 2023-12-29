package app.utilities;

import app.properties.PlayableEntity;

import java.util.Comparator;

public class SortAlphabetical implements Comparator<PlayableEntity> {
    /**
     * Compares the names of the 2 entities.
     * @param o1 The first entity whose name will be compared
     * @param o2 The second entity whose name will be compared
     * @return A positive integer,  if the first entity name is greater than the second one,
     * {@code 0}, if they're equal, or a negative integer, if the second entity name is
     * greater that the first one
     */
    @Override
    public int compare(final PlayableEntity o1, final PlayableEntity o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
