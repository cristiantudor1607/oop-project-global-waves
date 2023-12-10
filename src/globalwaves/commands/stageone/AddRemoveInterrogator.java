package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stageone.AddRemoveExit;
import globalwaves.commands.outputs.stageone.AddRemoveOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class AddRemoveInterrogator extends CommandObject {
    private int playlistId;
    @JsonIgnore
    private AddRemoveExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestAddRemove(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new AddRemoveOutput(this)).generateOutputNode();
    }
}
