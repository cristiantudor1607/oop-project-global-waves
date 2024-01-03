package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.outputs.stageone.StatisticsOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OnlineUsersInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> results;

    /**
     * Executes the getOnlineUsers command.
     */
    @Override
    public void execute() {
        results = manager.requestOnlineUsers();
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        // Link https://www.baeldung.com/java-jackson-remove-json-elements
        JsonNode output =  (new StatisticsOutput(this)).generateOutputNode();
        ObjectNode object = (ObjectNode) output;
        object.remove("user");

        return output;
    }
}
