package app.utilities;

import app.properties.NamePossessor;

import java.util.Comparator;
import java.util.Map;

public class SortByKeyName<T extends NamePossessor, K> implements Comparator<Map.Entry<T, K>> {
    /**
     * Compares the keys of the map entries, by their name.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return 0 if the argument o2 is equal to the argument o1;
     * a value less than 0 if o1 has a name lexicographically less than the o2 name;
     * and a value greater than 0 if o1 has a name lexicographically greater than the o2 name.
     */
    @Override
    public int compare(final Map.Entry<T, K> o1, final Map.Entry<T, K> o2) {
        String name1 = o1.getKey().getName();
        String name2 = o2.getKey().getName();

        return name1.compareTo(name2);
    }
}
