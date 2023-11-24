package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class NextOutput extends CommandOutputFormatter {
    private String message;

    public NextOutput(NextInterrogator executedQuery) {
        command = "next";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }
}

@Getter
public class NextInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    @Override
    public JsonNode execute(ActionManager manager) {
        message = manager.requestNext(this);
        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);
        return (new NextOutput(this)).generateOutputNode();
    }
}
