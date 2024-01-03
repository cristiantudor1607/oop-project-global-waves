package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.FollowExit;
import app.outputs.stageone.FollowOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class FollowInterrogator extends CommandObject {
    @JsonIgnore
    private FollowExit.Status exitStatus;

    /**
     * Executes the follow command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestFollowAction(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new FollowOutput(this)).generateOutputNode();
    }
}
