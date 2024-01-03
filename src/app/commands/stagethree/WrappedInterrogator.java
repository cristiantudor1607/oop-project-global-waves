package app.commands.stagethree;

import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

public class WrappedInterrogator extends CommandObject {
    @JsonIgnore
    Map<String, List<Map.Entry<String, Integer>>> statistics;

    /**
     * Executes the Wrapped command.
     */
    @Override
    public void execute() {
        statistics = manager.requestWrap(username);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     *
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return null;
    }
}
