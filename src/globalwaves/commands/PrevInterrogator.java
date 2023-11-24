package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class PrevOutput extends CommandOutputFormatter {
    private final String message;

    public PrevOutput(PrevInterrogator executedQuery) {
        command = "prev";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }
}

@Getter
public class PrevInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    @Override
    public JsonNode execute(ActionManager manager) {
        message = manager.requestPrev(this);

        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new PrevOutput(this)).generateOutputNode();
    }
}
