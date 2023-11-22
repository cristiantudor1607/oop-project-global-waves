package globalwaves.parser.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public abstract class CommandOutputFormatter {
    protected String command;
    protected String user;
    protected int timestamp;

    public JsonNode generateOutputNode() {
        ObjectMapper formatter = new ObjectMapper();
        return formatter.valueToTree(this);
    }
}
