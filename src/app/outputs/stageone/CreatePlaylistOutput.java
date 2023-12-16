package app.outputs.stageone;

import app.exitstats.stageone.CreationExit;
import app.commands.stageone.CreatePlaylistInterrogator;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class CreatePlaylistOutput extends CommandOutputFormatter {
    private final String message;

    public CreatePlaylistOutput(final CreatePlaylistInterrogator executedQuery) {
        command = "createPlaylist";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final CreationExit.Status atExit) {
        return switch (atExit) {
            case CREATED -> StringConstants.CREATE_SUCCESS;
            case ALREADY_EXISTS -> StringConstants.PLAYLISTS_EXIST;
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
