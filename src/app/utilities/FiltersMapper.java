package app.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;

import java.util.*;

public class FiltersMapper {
    private FiltersMapper() {}

    /**
     * Converts the JsonNode that contains the {@code filters} into a
     * {@code Map<String, List<String>>}. Only the "<b>tags</b>" filter uses a
     * list for its values, but most of the time, the first element of the list
     * is the value.
     * @param filtersNode The node containing the filters.
     * @return The filters converted into a {@code Map<String, List<String>>}
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
