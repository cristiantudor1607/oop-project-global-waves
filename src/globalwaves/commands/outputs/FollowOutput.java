package globalwaves.commands.outputs;

import globalwaves.commands.FollowInterrogator;
import globalwaves.commands.enums.exitcodes.FollowExit;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class FollowOutput extends CommandOutputFormatter {
    private final String message;

    public FollowOutput(final FollowInterrogator executedQuery) {
        command = "follow";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = getExitMessage(executedQuery.getExitCode());
    }

    public String getExitMessage(final FollowExit.Code exitCode) {
        return switch (exitCode) {
            case FOLLOWED -> "Playlist followed successfully.";
            case UNFOLLOWED -> "Playlist unfollowed successfully.";
            case NOT_A_PLAYLIST -> "The selected source is not a playlist.";
            case NO_SOURCE -> "Please select a source before following or unfollowing.";
            case OWNER -> "You cannot follow or unfollow your own playlist.";
        };
    }
}
