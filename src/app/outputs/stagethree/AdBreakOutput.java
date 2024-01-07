package app.outputs.stagethree;

import app.commands.stagethree.AdBreakInterrogator;
import app.exitstats.stagethree.AdBreakExit;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AdBreakOutput extends CommandOutputFormatter {
    private final String message;

    public AdBreakOutput(final AdBreakInterrogator executedQuery) {
        command = "adBreak";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    /**
     * Generates a user-specific exit message based on the exit code provided.
     * @param username The name of the user that received the ad break
     * @param atExit The exit code sent by manager
     * @return A user-specific message
     */
    public String generateMessage(final String username, final AdBreakExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_PLAYING -> username + " is not playing any music.";
            case SUCCESS -> "Ad inserted successfully.";
            case UNKNOWN -> "Unknown error.";
        };
    }
}
