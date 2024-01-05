package app.utilities;

import app.properties.PlayableEntity;

import java.util.Comparator;

public class SortByCreatorId implements Comparator<PlayableEntity> {
    // TODO: Add doc
    /**
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(PlayableEntity o1, PlayableEntity o2) {
        return o1.getCreatorIdForSorting() - o2.getCreatorIdForSorting();
    }
}
