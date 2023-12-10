package globalwaves.commands.outputs.stageone;

import globalwaves.commands.stageone.CreatePlaylistInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class CreatePlaylistOutput extends CommandOutputFormatter {
    private String message;

    public CreatePlaylistOutput(final CreatePlaylistInterrogator executedQuery) {
        command = "createPlaylist";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case CREATED -> message = "Playlist created successfully.";
            case ALREADY_EXISTS -> message = "A playlist with the same name already exists.";
            default -> {

            }
        }
    }
}
