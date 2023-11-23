package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class RepeatOutput extends CommandOutputFormatter {
    private final String message;

    public RepeatOutput(RepeatInterrogator executedQuery) {
        command = "repeat";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getExitMessage();
    }
}

@Getter
public class RepeatInterrogator extends CommandObject {
    @JsonIgnore
    private String exitMessage;

    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Changing repeat state ...");

        exitMessage = manager.requestRepeatAction(this);
        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new RepeatOutput(this)).generateOutputNode();
    }
}
