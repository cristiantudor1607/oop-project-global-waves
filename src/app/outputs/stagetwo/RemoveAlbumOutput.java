package app.outputs.stagetwo;

import app.exitstats.stagetwo.RemoveAlbumExit;
import app.commands.stagetwo.RemoveAlbumInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class RemoveAlbumOutput extends CommandOutputFormatter {
    private final String message;

    public RemoveAlbumOutput(final RemoveAlbumInterrogator executedQuery) {
        command = "removeAlbum";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final RemoveAlbumExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_ARTIST -> username + " is not an artist.";
            case DONT_HAVE -> username + " doesn't have an album with the given name.";
            case FAIL -> username + " can't delete this album.";
            case SUCCESS -> username + " deleted the album successfully.";
        };
    }
}
