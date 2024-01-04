package app.outputs.stagethree;

import app.commands.stagethree.WrappedInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import app.statistics.StatisticsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

@Getter
public class WrappedOutput extends CommandOutputFormatter {
    @JsonIgnore
    private final StatisticsTemplate result;

    public WrappedOutput(final WrappedInterrogator executedQuery) {
        command = "wrapped";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getStatistics();
    }

    @Override
    public JsonNode generateOutputNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.put("command", command);
        mainNode.put("user", user);
        mainNode.put("timestamp", timestamp);

        JsonNode resultNode = result.generateStatisticsNode();
        mainNode.set("result", resultNode);

        return mainNode;
    }
}
