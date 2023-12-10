package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.SelectExit;
import globalwaves.commands.outputs.SearchOutput;
import globalwaves.commands.outputs.SelectOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class SelectInterrogator extends CommandObject {
    private int itemNumber;
    @JsonIgnore private PlayableEntity selectedAudio = null;
    @JsonIgnore private SelectExit.Code exitCode;

    @Override
    public void execute() {
        exitCode = manager.requestItemSelection(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new SelectOutput(this)).generateOutputNode();
    }
}
