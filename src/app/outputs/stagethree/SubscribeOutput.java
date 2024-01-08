package app.outputs.stagethree;

import app.commands.stagethree.SubscribeInterrogator;
import app.exitstats.stagethree.SubscribeExit;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class SubscribeOutput extends CommandOutputFormatter {
    private final String message;

    public SubscribeOutput(final SubscribeInterrogator executedQuery) {
        command = "subscribe";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(executedQuery.getExitContainer());
    }

    /**
     * Generates a user-specific exit message, based on the data encapsulated in the
     * container.
     * @param container The data sent by manager, after executing the command
     * @return A user-specific message
     */
    public String generateMessage(final SubscribeExit container) {
        String username = container.getUsername();
        String publicName = container.getPublicName();
        SubscribeExit.Status atExit = container.getStatus();

        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_ON_PAGE -> "To subscribe you need to be on the page of an artist or host.";
            case SUBSCRIBED -> username + " subscribed to " + publicName + " successfully.";
            case UNSUBSCRIBED -> username + " unsubscribed from "
                    + publicName + " successfully.";
        };
    }
}
