package app.outputs.stageone;

import app.commands.stageone.PrevInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class PrevOutput extends CommandOutputFormatter {
    private final String message;

    public PrevOutput(final PrevInterrogator executedQuery) {
        command = "prev";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }
}
