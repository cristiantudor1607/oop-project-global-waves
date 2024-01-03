package app.utilities;

import app.properties.PlayableEntity;

import java.util.Comparator;

public class SortByCreationTime implements Comparator<PlayableEntity> {
    /**
     * Compares the creation time of the entities.
     * @param o1 The first entity whose creation time will be compared
     * @param o2 The second entity whose creation time will be compared
     * @return A positive integer, if the first entity was created after the second
     * one, {@code 0}, if they were created at the same time, or a negative integer,
     * if the first entity was created before the second one
     */
    @Override
    public int compare(final PlayableEntity o1, final PlayableEntity o2) {
        return o1.getCreationTime() - o2.getCreationTime();
    }
}
