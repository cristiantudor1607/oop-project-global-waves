package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class BackwardInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    @Override
    public JsonNode execute(ActionManager manager) {
        message = manager.requestBackward(this);
        manager.setLastActionTime(timestamp);

        return (new ForwardBackwardOutput(this)).generateOutputNode();
    }
}
