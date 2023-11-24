package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class ForwardInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    @Override
    public JsonNode execute(ActionManager manager) {
        message = manager.requestForward(this);

        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);
        return (new ForwardBackwardOutput(this)).generateOutputNode();
    }
}
