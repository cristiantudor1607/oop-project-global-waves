package app.search.engines;

import app.pages.Page;
import app.properties.PlayableEntity;

import java.util.ArrayList;
import java.util.List;

public class EngineResultsParser {
    private EngineResultsParser() {}
    public static int TRUNC_SIZE = 5;
    public static <T> List<T>
    getRelevantResults(List<T> allResults) {
        if (allResults.size() <= TRUNC_SIZE)
            return allResults;

        return allResults.subList(0, 5);
    }

    public static List<String>
    getNamesFromList(List<? extends PlayableEntity> namedEntities) {
        List<String> names = new ArrayList<>();

        for (PlayableEntity entity : namedEntities)
            names.add(entity.getName());

        return names;
    }

    public static List<String> getUsernamesFromPages(List<Page> pages) {
        List<String> usernames = new ArrayList<>();

        for (Page page: pages)
            usernames.add(page.getUsername());

        return usernames;
    }

}
