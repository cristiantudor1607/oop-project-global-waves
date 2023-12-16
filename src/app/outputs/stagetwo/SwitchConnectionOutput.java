package app.outputs.stagetwo;

import app.commands.stagetwo.ConnectionInterrogator;
import app.exitstats.stagetwo.SwitchConnectionExit;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class SwitchConnectionOutput extends CommandOutputFormatter {
    private final String message;
    public SwitchConnectionOutput(final ConnectionInterrogator executedQuery) {
        command = "switchConnectionStatus";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(executedQuery.getUsername(), executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, SwitchConnectionExit.Status exitStatus) {
        return switch (exitStatus) {
            case INVALID_USERNAME -> "The username " + username + " doesn't exist.";
            case NOT_NORMAL -> username + " is not a normal user.";
            case SUCCESS -> username + " has changed status successfully.";
        };
    }
}
