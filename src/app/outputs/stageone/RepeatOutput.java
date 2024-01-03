package app.outputs.stageone;

import app.commands.stageone.RepeatInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class RepeatOutput extends CommandOutputFormatter {
    private final String message;

    public RepeatOutput(final RepeatInterrogator executedQuery) {
        command = "repeat";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }
}
