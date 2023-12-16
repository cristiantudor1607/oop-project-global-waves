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

    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = SwitchVisibilityExit.Status.OFFLINE;
            return;
        }

        exitStatus = manager.requestSwitchVisibility(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new SwitchVisibilityOutput(this)).generateOutputNode();
    }
}
