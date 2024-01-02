package app.outputs.stageone;

import app.exitstats.stageone.AddRemoveExit;
import app.commands.stageone.AddRemoveInterrogator;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddRemoveOutput extends CommandOutputFormatter {
    private final String message;

    public AddRemoveOutput(final AddRemoveInterrogator executedQuery) {
        command = "addRemoveInPlaylist";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(executedQuery.getUsername(), executedQuery.getExitStatus());
    }

    /**
     * Generates the exit message for addRemoveInPlaylist command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final AddRemoveExit.Status atExit) {
        return switch (atExit) {
            case ADDED -> StringConstants.ADDED_TO_PLAYLIST;
            case REMOVED -> StringConstants.REMOVED_FROM_PLAYLIST;
            case NOT_A_SONG -> StringConstants.NOT_A_SONG;
            case INVALID_PLAYLIST -> StringConstants.PLAYLIST_NOT_EXIST;
            case NO_SOURCE -> StringConstants.ADD_REMOVE_NO_SOURCE;
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
