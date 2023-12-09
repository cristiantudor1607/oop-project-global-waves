package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.SwitchVisibilityExit;
import globalwaves.commands.outputs.SwitchVisibilityOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SwitchVisibilityInterrogator extends CommandObject {
    private int playlistId;
    @JsonIgnore
    private SwitchVisibilityExit.Code exitCode;

    @Override
    public JsonNode execute(ActionManager manager) {
        exitCode = manager.requestSwitch(this);
        manager.setLastActionTime(timestamp);

        return (new SwitchVisibilityOutput(this)).generateOutputNode();
    }
}
