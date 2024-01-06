package app.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class StatisticsTemplate {
    public JsonNode generateStatisticsNode() {
        ObjectMapper formatter = new ObjectMapper();
        return formatter.valueToTree(this);
    }

    /**
     * If there are no statistics, it returns the user-specific "No data to show" message.
     * @return {@code null}, if the user has statistics, the "No data to show" message,
     * otherwise
     */
    @JsonIgnore
    public String getSpecificMessage() {
        return null;
    }
}
