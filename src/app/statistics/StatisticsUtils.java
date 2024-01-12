package app.statistics;

import app.properties.NamePossessor;
import app.properties.PlayableEntity;
import app.utilities.HelperTool;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StatisticsUtils {
    private static final HelperTool TOOL = HelperTool.getInstance();

    private StatisticsUtils() { }

    /**
     * Takes a history of any named objects, and returns the most listened objects.
     * Before making the ranking, it combines the number of listens for objects with
     * equal names.
     * @param history The history to be inspected
     * @param compareFunc The comparator
     * @return A list of {@code <String, Integer>} tuples, containing the first {@code 5} results,
     * or less if there aren't {@code 5} records in history.
     * @param <T> The type of objects in history. They have to implement the {@code NamedObject}
     *           interface, in order to call the {@code getName} method.
     */
    public static <T extends NamePossessor> List<Map.Entry<String, Integer>>
    combineAndParseAlbumHistory(final Map<T, Integer> history,
                                final Comparator<Map.Entry<String, Integer>> compareFunc) {
        Map<String, Integer> combinedMap = new HashMap<>();
        history.forEach((object, counter) -> {
            String name = object.getName();
            int listens = combinedMap.getOrDefault(name, 0);
            combinedMap.put(name, listens + counter);
        });

        List<Map.Entry<String, Integer>> unrolled = new ArrayList<>(combinedMap.entrySet());
        unrolled.sort(compareFunc);
        TOOL.truncateResults(unrolled);
        return unrolled;
    }

    /**
     * Takes a history on any named objects, and returns the most listened objects.
     * @param history The history to be inspected
     * @param compareFunc The comparator
     * @return A list of {@code <String, Integer>} tuples, containing the first {@code 5} results,
     * or less if there aren't {@code 5} records in history.
     * @param <T> The type of objects in history. They have to implement the {@code NamedObject}
     *           interface, to call the {@code getName} method.
     */
    public static <T extends NamePossessor> List<Map.Entry<String, Integer>>
    parseHistory(final Map<T, Integer> history,
                 final Comparator<Map.Entry<T, Integer>> compareFunc) {
        List<Map.Entry<T, Integer>> unrolled = new ArrayList<>(history.entrySet());
        unrolled.sort(compareFunc);
        TOOL.truncateResults(unrolled);
        return TOOL.extractNames(unrolled);
    }
}
