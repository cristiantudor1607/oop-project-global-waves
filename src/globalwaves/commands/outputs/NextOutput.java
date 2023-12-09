package globalwaves.commands.outputs;

import globalwaves.commands.NextInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
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
