package app.outputs.stagethree;

import app.commands.stagethree.BuyPremiumInterrogator;
import app.exitstats.stagethree.ChangeSubscriptionExit;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class BuyPremiumOutput extends CommandOutputFormatter {
    private final String message;

    public BuyPremiumOutput(final BuyPremiumInterrogator executedQuery) {
        command = "buyPremium";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    /**
     * Generates a message for buyPremium command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username,
                                  final ChangeSubscriptionExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case ALREADY_SUBS -> username + " is already a premium user.";
            case SUCCESS -> username + " bought the subscription successfully.";
        };
    }
}
