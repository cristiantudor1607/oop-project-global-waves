package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.LikeExit;
import app.outputs.stageone.LikeOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class LikeInterrogator extends CommandObject {
    @JsonIgnore
    private LikeExit.Status exitStatus;

    /**
     * Executes the like command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestLikeAction(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new LikeOutput(this)).generateOutputNode();
    }
}
