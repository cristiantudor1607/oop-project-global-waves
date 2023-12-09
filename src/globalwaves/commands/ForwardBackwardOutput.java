package globalwaves.commands;

import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class ForwardBackwardOutput extends CommandOutputFormatter {
    private final String message;

    public ForwardBackwardOutput(BackwardInterrogator executedQuery) {
        command = "backward";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }

    public ForwardBackwardOutput(ForwardInterrogator executedQuery) {
        command = "forward";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }
}
