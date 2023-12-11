package globalwaves.commands.outputs.stageone;

import globalwaves.commands.stageone.FollowInterrogator;
import globalwaves.commands.enums.exitstats.stageone.FollowExit;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class FollowOutput extends CommandOutputFormatter {
    private final String message;

    public FollowOutput(final FollowInterrogator executedQuery) {
        command = "follow";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = getExitMessage(user, executedQuery.getExitStatus());
    }

    public String getExitMessage(final String username, final FollowExit.Status exitStatus) {
        return switch (exitStatus) {
            case FOLLOWED -> "Playlist followed successfully.";
            case UNFOLLOWED -> "Playlist unfollowed successfully.";
            case NOT_A_PLAYLIST -> "The selected source is not a playlist.";
            case NO_SOURCE -> "Please select a source before following or unfollowing.";
            case OWNER -> "You cannot follow or unfollow your own playlist.";
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
