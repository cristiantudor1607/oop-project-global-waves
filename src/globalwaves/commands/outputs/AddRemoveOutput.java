package globalwaves.commands.outputs;

import globalwaves.commands.AddRemoveInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddRemoveOutput extends CommandOutputFormatter {
    private String message;

    public AddRemoveOutput(final AddRemoveInterrogator executedQuery) {
        command = "addRemoveInPlaylist";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case ADDED -> message = "Successfully added to playlist.";
            case REMOVED -> message = "Successfully removed from playlist.";
            case NOT_A_SONG -> message = "The loaded source is not a song.";
            case INVALID_PLAYLIST -> message = "The specified playlist does not exist.";
            case NO_SOURCE -> message = "Please load a source before adding to or "
                    + "removing from the playlist.";
            default -> {

            }
        }
    }
}
