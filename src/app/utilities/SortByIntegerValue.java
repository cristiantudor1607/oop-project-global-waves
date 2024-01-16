package app.utilities;

import java.util.Comparator;
import java.util.Map;

public class SortByIntegerValue<T> implements Comparator<Map.Entry<T, Integer>> {

    /**
     * Compares the integer stored in the values of the map entry.
     *
     * @param o1 the first value to be compared.
     * @param o2 the second value to be compared.
     * @return A positive integer, if the first value is bigger than the second one, {@code 0},
     * if they're equal, or a negative integer, if the second value is bigger than the first
     * one
     */
    @Override
    public int compare(final Map.Entry<T, Integer> o1,
                       final Map.Entry<T, Integer> o2) {
        return o1.getValue() - o2.getValue();
    }
}
