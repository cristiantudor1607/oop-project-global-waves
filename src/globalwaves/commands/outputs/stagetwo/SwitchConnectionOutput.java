package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.stagetwo.ConnectionInterrogator;
import globalwaves.commands.enums.exitstats.stagetwo.SwitchConnectionExit;
import globalwaves.parser.templates.CommandOutputFormatter;
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
