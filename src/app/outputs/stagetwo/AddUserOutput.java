package app.outputs.stagetwo;

import app.exitstats.stagetwo.AddUserExit;
import app.commands.stagetwo.AddUserInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddUserOutput extends CommandOutputFormatter {
    private final String message;

    public AddUserOutput(final AddUserInterrogator executedQuery) {
        command = "addUser";
        timestamp = executedQuery.getTimestamp();
        user = executedQuery.getUsername();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    /**
     * Generates a message for addUser command.
     * @param username The name of the user that should have been added
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final AddUserExit.Status atExit) {
        return switch (atExit) {
            case USERNAME_TAKEN -> "The username " + username + " is already taken.";
            case SUCCESS -> "The username " + username + " has been added successfully.";
            case ERROR -> "Unknown user type.";
        };
    }
}
