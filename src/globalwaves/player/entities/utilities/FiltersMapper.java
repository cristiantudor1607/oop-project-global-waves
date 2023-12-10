package globalwaves.player.entities.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;

import java.util.*;

public class FiltersMapper {
    private FiltersMapper() {}

    /**
     * Method that turns a node with filters into a useful Map with keys as Strings
     * and values as ArrayLists of Strings
     * @param filtersNode The JsonNode that contains the filters data
     * @return A Map with all the filters found
     */
    public static Map<String, List<String>> convertToMap(@NonNull JsonNode filtersNode) {
        Map<String, List<String>> mappedFilters = new HashMap<>();

        Iterator<Map.Entry<String, JsonNode>> filters = filtersNode.fields();
        while (filters.hasNext()) {
            Map.Entry<String, JsonNode> entry = filters.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            if (value.isArray()) {
                List<String> tagsList = new ArrayList<>();
                for (JsonNode tag: value)
                    tagsList.add(tag.asText());

                mappedFilters.put(key, tagsList);
                continue;
            }

            List<String> singleValue = new ArrayList<>();
            singleValue.add(value.asText());
            mappedFilters.put(key, singleValue);
        }

        return mappedFilters;
    }
}
