package app.statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class StatisticsTemplate {
    public JsonNode generateStatisticsNode() {
        ObjectMapper formatter = new ObjectMapper();
        return formatter.valueToTree(this);
    }
}
