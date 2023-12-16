package app.outputs.stagetwo;

import app.exitstats.stagetwo.RemoveEventExit;
import app.commands.stagetwo.RemoveEventInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class RemoveEventOutput extends CommandOutputFormatter {
    private final String message;

    public RemoveEventOutput(final RemoveEventInterrogator executedQuery) {
        command = "removeEvent";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final RemoveEventExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_ARTIST -> username + " is not an artist.";
            case INVALID_NAME -> username + " doesn't have an event with the given name.";
            case SUCCESS -> username + " deleted the event successfully.";
        };
    }
}
