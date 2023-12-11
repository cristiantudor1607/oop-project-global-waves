package globalwaves.commands.outputs.stageone;

import globalwaves.commands.enums.exitstats.stageone.AddRemoveExit;
import globalwaves.commands.stageone.AddRemoveInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddRemoveOutput extends CommandOutputFormatter {
    private String message;

    public AddRemoveOutput(final AddRemoveInterrogator executedQuery) {
        command = "addRemoveInPlaylist";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(executedQuery.getUsername(), executedQuery.getExitStatus());
    }

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
