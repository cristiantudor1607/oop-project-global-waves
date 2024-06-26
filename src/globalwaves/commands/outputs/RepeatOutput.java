package globalwaves.commands.outputs;

import globalwaves.commands.RepeatInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class RepeatOutput extends CommandOutputFormatter {
    private final String message;

    public RepeatOutput(final RepeatInterrogator executedQuery) {
        command = "repeat";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getExitMessage();
    }
}
