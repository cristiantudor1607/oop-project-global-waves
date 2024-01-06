package app.outputs.stagethree;

import app.commands.stagethree.BuyMerchInterrogator;
import app.exitstats.stagethree.BuyMerchExit;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class BuyMerchOutput extends CommandOutputFormatter {
    private final String message;

    public BuyMerchOutput(final BuyMerchInterrogator executedQuery) {
        command = "buyMerch";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getName(),
                executedQuery.getExitStatus());
    }

    /**
     * Generates a user-specific exit message for buyMerch command.
     * @param username The name of the user that gave the command
     * @param merchName The name of the merch it wanted to buy
     * @param atExit The exit code sent by manager
     * @return A user-specific message
     */
    public String generateMessage(final String username, final String merchName,
                                  final BuyMerchExit.Status atExit) {
        return switch (atExit) {
            case USER_DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_ON_ARTIST_PAGE -> "Cannot buy merch from this page.";
            case MERCH_DOESNT_EXIST -> "The merch " + merchName + " doesn't exist.";
            case SUCCESS -> username + " has added new merch successfully.";
        };
    }
}
