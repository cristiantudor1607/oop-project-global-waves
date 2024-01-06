package app.utilities;

import app.properties.NamePossessor;
import app.properties.UniqueIdPossessor;

import java.util.Comparator;
import java.util.Map;

public class SortByKeyName<T extends NamePossessor, K> implements Comparator<Map.Entry<T, K>> {
    // TODO: Add doc
    /**
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(Map.Entry<T, K> o1, Map.Entry<T, K> o2) {
        String name1 = o1.getKey().getName();
        String name2 = o2.getKey().getName();

        return name1.compareTo(name2);
    }
}
