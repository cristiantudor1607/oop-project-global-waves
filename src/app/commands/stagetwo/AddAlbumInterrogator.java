package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.AddAlbumExit;
import app.outputs.stagetwo.AddAlbumOutput;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AddAlbumInterrogator extends CommandObject {
    @JsonProperty("name")
    private String albumName;
    private int releaseYear;
    private String description;
    private List<Song> songs;
    @JsonIgnore
    private AddAlbumExit.Status exitStatus;

    /**
     * Executes the addAlbum command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestAddingAlbum(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new AddAlbumOutput(this)).generateOutputNode();
    }
}
