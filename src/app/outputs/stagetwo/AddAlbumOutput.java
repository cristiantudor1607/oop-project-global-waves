package app.outputs.stagetwo;

import app.exitstats.stagetwo.AddAlbumExit;
import app.commands.stagetwo.AddAlbumInterrogator;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddAlbumOutput extends CommandOutputFormatter {
    private final String message;

    public AddAlbumOutput(final AddAlbumInterrogator executedQuery) {
        command = "addAlbum";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    /**
     * Generates a message for addAlbum command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final AddAlbumExit.Status atExit) {
        return switch (atExit) {
            case SUCCESS -> username + StringConstants.ADD_ALBUM_SUCCESS;
            case NOT_ARTIST -> username + StringConstants.IS_NOT_ARTIST;
            case SAME_NAME -> username + StringConstants.ALBUM_SAME_NAME;
            case SAME_SONG -> username + StringConstants.ALBUM_SAME_SONG;
            case INVALID_USERNAME -> "The username " + username + StringConstants.DOESNT_EXIST;
        };
    }
}
