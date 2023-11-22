package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.CreationExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class CreatePlaylistOutput extends CommandOutputFormatter {
    private String message;

    public CreatePlaylistOutput(CreatePlaylistInterrogator executedQuery) {
        command = "createPlaylist";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case CREATED -> message = "Playlist created successfully.";
            case ALREADY_EXISTS -> message = "A playlist with the same name already exists.";
        }
    }
}

@Getter
public class CreatePlaylistInterrogator extends CommandObject {
    private String playlistName;
    @JsonIgnore private CreationExit.code exitCode;
    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Creating playlist ...");

        exitCode = manager.requestPlaylistCreation(this);
        manager.setLastActionTime(timestamp);
        
        return (new CreatePlaylistOutput(this)).generateOutputNode();
    }
}
