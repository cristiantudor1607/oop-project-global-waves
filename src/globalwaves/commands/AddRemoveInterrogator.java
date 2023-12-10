package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.AddRemoveExit;
import globalwaves.commands.outputs.AddRemoveOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class AddRemoveInterrogator extends CommandObject {
    private int playlistId;
    @JsonIgnore
    private AddRemoveExit.Code exitCode;

    @Override
    public void execute() {
        exitCode = manager.requestAddRemove(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new AddRemoveOutput(this)).generateOutputNode();
    }
}
