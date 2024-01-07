package app.utilities;

import java.util.Comparator;
import java.util.Map;

public class SortByIntegerValue<T> implements Comparator<Map.Entry<T, Integer>> {

    // TODO: Add doc
    // TODO: Inverseaza peste tot aceasta sortare si adauga .reversed
    /**
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(final Map.Entry<T, Integer> o1,
                       final Map.Entry<T, Integer> o2) {
        return o2.getValue() - o1.getValue();
    }
}
