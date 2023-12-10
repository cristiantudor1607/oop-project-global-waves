package globalwaves.commands.outputs.stageone;

import globalwaves.commands.stageone.BackwardInterrogator;
import globalwaves.commands.stageone.ForwardInterrogator;
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
