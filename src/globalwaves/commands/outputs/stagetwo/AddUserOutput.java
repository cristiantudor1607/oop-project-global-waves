package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.enums.exitstats.stagetwo.AddUserExit;
import globalwaves.commands.stagetwo.AddUserInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddUserOutput extends CommandOutputFormatter {
    private String message;

    public AddUserOutput(final AddUserInterrogator executedQuery) {
        command = "addUser";
        timestamp = executedQuery.getTimestamp();
        user = executedQuery.getUsername();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, AddUserExit.Status atExit) {
        return switch (atExit) {
            case USERNAME_TAKEN -> "The username " + username + " is already taken.";
            case SUCCESS -> "The username " + username + " has been added successfully.";
            case ERROR -> "Unknown user type.";
        };
    }
}