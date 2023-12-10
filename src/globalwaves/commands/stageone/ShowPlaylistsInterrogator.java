package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.stageone.ShowPlaylistsOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.Playlist;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowPlaylistsInterrogator extends CommandObject {
    @JsonIgnore
    private List<Playlist> userPlaylists;

    /**
     * Method that generates the output based on the ShowPlaylistsInterrogator current instance.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode
     */
    @Override
    public void execute() {
        userPlaylists = manager.requestOwnerPlaylists(username);

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ShowPlaylistsOutput(this)).generateOutputNode();
    }
}
