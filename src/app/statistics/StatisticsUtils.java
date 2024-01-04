package app.statistics;

import app.properties.NamedObject;
import app.utilities.HelperTool;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StatisticsUtils {
    private static final HelperTool tool = HelperTool.getInstance();

    public static <T extends NamedObject> List<Map.Entry<String, Integer>>
    parseHistory(final Map<T, Integer> history,
                 final Comparator<Map.Entry<T, Integer>> compareFunc) {
        List<Map.Entry<T, Integer>> unrolled = new ArrayList<>(history.entrySet());
        unrolled.sort(compareFunc);
        tool.truncateResults(unrolled);
        return tool.extractNames(unrolled);
    }
}
