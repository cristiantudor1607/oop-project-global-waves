package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.LoadExit;
import app.outputs.stageone.LoadOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class LoadInterrogator extends CommandObject {
    @JsonIgnore private LoadExit.Status exitStatus;

    /**
     * Executes the load command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestLoading(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new LoadOutput(this)).generateOutputNode();
    }
}
