package app.utilities;

import app.properties.PlayableEntity;

import java.util.Comparator;

public class SortByCreatorId implements Comparator<PlayableEntity> {
    /**
     * Compares the ids of the entities' creators.
     * If the users aren't artists, the compare will consider them equal.
     *
     * @param o1 The first entity to be compared
     * @param o2 The second entity to be compared
     * @return A positive integer, if the first entity has the creator with the bigger id,
     * {@code 0}, if the creators have the same id (theoretically case), or a negative integer
     * if the second entity has the creator with the bigger id.
     * The comparator was designed for extension, to generalize this type of comparison, but
     * for our case, it works only on album, because those are the only entities whose
     * {@code getCreatorIdForSorting} doesn't return {@code 0}.
     */
    @Override
    public int compare(final PlayableEntity o1, final PlayableEntity o2) {
        return o1.getCreatorIdForSorting() - o2.getCreatorIdForSorting();
    }
}
