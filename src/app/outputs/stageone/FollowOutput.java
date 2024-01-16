package app.outputs.stageone;

import app.commands.stageone.FollowInterrogator;
import app.exitstats.stageone.FollowExit;
import app.utilities.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class FollowOutput extends CommandOutputFormatter {
    private final String message;

    public FollowOutput(final FollowInterrogator executedQuery) {
        command = "follow";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    /**
     * Generates a message for follow command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final FollowExit.Status atExit) {
        return switch (atExit) {
            case FOLLOWED -> "Playlist followed successfully.";
            case UNFOLLOWED -> "Playlist unfollowed successfully.";
            case NOT_A_PLAYLIST -> "The selected source is not a playlist.";
            case NO_SOURCE -> "Please select a source before following or unfollowing.";
            case OWNER -> "You cannot follow or unfollow your own playlist.";
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
