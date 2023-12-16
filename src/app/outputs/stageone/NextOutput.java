package app.outputs.stageone;

import app.commands.stageone.NextInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class NextOutput extends CommandOutputFormatter {
    private final String message;

    public NextOutput(final NextInterrogator executedQuery) {
        command = "next";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }
}
