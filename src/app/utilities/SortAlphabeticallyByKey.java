package app.utilities;

import java.util.Comparator;
import java.util.Map;

public class SortAlphabeticallyByKey<T> implements Comparator<Map.Entry<String, T>> {
    @Override
    public int compare(final Map.Entry<String, T> o1, final Map.Entry<String, T> o2) {
        String name1 = o1.getKey();
        String name2 = o2.getKey();

        return name1.compareTo(name2);
    }
}
