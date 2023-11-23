package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.AddRemoveExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class AddRemoveOutput extends CommandOutputFormatter {
    private String message;

    public AddRemoveOutput(AddRemoveInterrogator executedQuery) {
        command = "addRemoveInPlaylist";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case ADDED -> message = "Successfully added to playlist.";
            case REMOVED -> message = "Successfully removed from playlist.";
            case NOT_A_SONG -> message = "The loaded source is not a song.";
            case INVALID_PLAYLIST -> message = "The specified playlist does not exist.";
            case NO_SOURCE -> message = "Please load a source before adding to or " +
                    "removing from the playlist.";
        }
    }
}

@Getter
public class AddRemoveInterrogator extends CommandObject {
    private int playlistId;
    // TODO: Are mai mult sens ca exitCode sa fie inaccesibil de alte obiecte
    @JsonIgnore
    private AddRemoveExit.code exitCode;

    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Adding or removing to playlist ...");

        exitCode = manager.requestAddRemove(this);
        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new AddRemoveOutput(this)).generateOutputNode();
    }
}
