package app.outputs.stagethree;

import app.commands.stagethree.SeeMerchInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.List;

@Getter
public class SeeMerchOutput extends CommandOutputFormatter {
    @JsonIgnore
    private final List<String> result;

    public SeeMerchOutput(final SeeMerchInterrogator executedQuery) {
        command = "seeMerch";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getItems();
    }

    /**
     * Method that generates the output JsonNode, based on the current instance of the
     * class.
     * @return The output formatted as JsonNode
     */
    @Override
    public JsonNode generateOutputNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.put("command", command);
        mainNode.put("user", user);
        mainNode.put("timestamp", timestamp);

        if (result != null) {
            ArrayNode resultNode = mapper.valueToTree(result);
            mainNode.set("result", resultNode);
        } else {
            mainNode.put("message", "The username " + user + " doesn't exist.");
        }

        return mainNode;
    }
}
