package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.SwitchVisibilityExit;
import app.outputs.stageone.SwitchVisibilityOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VisibilityInterrogator extends CommandObject {
    private int playlistId;
    @JsonIgnore
    private SwitchVisibilityExit.Status exitStatus;

    /**
     * Executes the switchVisibility command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestSwitchVisibility(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new SwitchVisibilityOutput(this)).generateOutputNode();
    }
}
