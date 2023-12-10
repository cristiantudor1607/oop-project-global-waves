package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.stageone.SwitchVisibilityExit;
import globalwaves.commands.outputs.stageone.SwitchVisibilityOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VisibilityInterrogator extends CommandObject {
    private int playlistId;
    @JsonIgnore
    private SwitchVisibilityExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestSwitchVisibility(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new SwitchVisibilityOutput(this)).generateOutputNode();
    }
}
