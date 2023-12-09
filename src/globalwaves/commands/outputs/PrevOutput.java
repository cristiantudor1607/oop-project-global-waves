package globalwaves.commands.outputs;

import globalwaves.commands.PrevInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
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
