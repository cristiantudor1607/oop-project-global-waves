package app.outputs.stagethree;

import app.commands.stagethree.CancelPremiumInterrogator;
import app.exitstats.stagethree.ChangeSubscriptionExit;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class CancelPremiumOutput extends CommandOutputFormatter {
    private final String message;

    public CancelPremiumOutput(final CancelPremiumInterrogator executedQuery) {
        command = "cancelPremium";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    /**
     * Generates the user-specific exit message for cancelPremium command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A user-specific message
     */
    public String generateMessage(final String username, ChangeSubscriptionExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case ALREADY_SUBS -> username + " is not a premium user.";
            case SUCCESS -> username + " cancelled the subscription successfully.";
        };
    }
}
