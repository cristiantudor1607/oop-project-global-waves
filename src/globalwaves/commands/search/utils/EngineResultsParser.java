package globalwaves.commands.search.utils;

import globalwaves.player.entities.properties.PlayableEntity;

import java.util.ArrayList;
import java.util.List;

public class EngineResultsParser {
    private EngineResultsParser() {}
    public static int TRUNC_SIZE = 5;
    public static List<? extends PlayableEntity>
    getRelevantResults(List<? extends PlayableEntity> allResults) {
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
}
