package app.outputs.stageone;

import app.commands.stageone.BackwardInterrogator;
import app.commands.stageone.ForwardInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
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
