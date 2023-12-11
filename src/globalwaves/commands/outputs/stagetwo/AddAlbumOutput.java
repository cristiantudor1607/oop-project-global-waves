package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.enums.exitstats.stagetwo.AddAlbumExit;
import globalwaves.commands.stagetwo.AddAlbumInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddAlbumOutput extends CommandOutputFormatter {
    private final String message;

    public AddAlbumOutput(AddAlbumInterrogator executedQuery) {
        command = "addAlbum";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

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
