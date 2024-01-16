package app.outputs.stageone;

import app.exitstats.stageone.LikeExit;
import app.commands.stageone.LikeInterrogator;
import app.utilities.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class LikeOutput extends CommandOutputFormatter {
    private final String message;

    public LikeOutput(final LikeInterrogator executedQuery) {
        command = "like";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(executedQuery.getUsername(), executedQuery.getExitStatus());
    }

    /**
     * Generates a message for like command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final LikeExit.Status atExit) {
        return switch (atExit) {
            case LIKED ->  "Like registered successfully.";
            case UNLIKED -> "Unlike registered successfully.";
            case NO_SOURCE -> "Please load a source before liking or unliking.";
            case NOT_A_SONG -> "Loaded source is not a song.";
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
