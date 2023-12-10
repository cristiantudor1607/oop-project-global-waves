package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.CreationExit;
import globalwaves.commands.outputs.CreatePlaylistOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class CreatePlaylistInterrogator extends CommandObject {
    private String playlistName;
    @JsonIgnore private CreationExit.Code exitCode;

    /**
     * Method that executes the CreatePlaylist Command and returns it's output
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        exitCode = manager.requestPlaylistCreation(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new CreatePlaylistOutput(this)).generateOutputNode();
    }
}
