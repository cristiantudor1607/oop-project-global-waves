package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.stageone.SelectExit;
import globalwaves.commands.outputs.stageone.SelectOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class SelectInterrogator extends CommandObject {
    private int itemNumber;
    @JsonIgnore private PlayableEntity selectedAudio = null;
    @JsonIgnore private SelectExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestItemSelection(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new SelectOutput(this)).generateOutputNode();
    }
}
