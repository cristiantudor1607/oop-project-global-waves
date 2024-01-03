package app.parser.commands.templates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public abstract class CommandOutputFormatter {
    protected String command;
    protected String user;
    protected int timestamp;

    /**
     * Method that generates the output JsonNode, based on the current instance of the
     * class.
     * @return The output formatted as JsonNode
     */
    public JsonNode generateOutputNode() {
        ObjectMapper formatter = new ObjectMapper();
        return formatter.valueToTree(this);
    }
}
