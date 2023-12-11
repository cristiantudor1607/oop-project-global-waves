package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.AddAlbumExit;
import globalwaves.commands.outputs.stagetwo.AddAlbumOutput;
import globalwaves.commands.outputs.stagetwo.AddUserOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.Song;
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

    @Override
    public void execute() {
        exitStatus = manager.requestAddingAlbum(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new AddAlbumOutput(this)).generateOutputNode();
    }
}
