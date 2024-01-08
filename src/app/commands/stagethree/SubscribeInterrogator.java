package app.commands.stagethree;

import app.exitstats.stagethree.SubscribeExit;
import app.outputs.stagethree.SubscribeOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class SubscribeInterrogator extends CommandObject {
    @JsonIgnore
    private SubscribeExit exitContainer;
    /**
     * Executes the subscribe command.
     */
    @Override
    public void execute() {
        exitContainer = manager.requestSubscribe(username);
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
        return new SubscribeOutput(this).generateOutputNode();
    }
}
