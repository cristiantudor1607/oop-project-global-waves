package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.stagetwo.ShowAlbumsOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.Album;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowAlbumsInterrogator extends CommandObject {
    @JsonIgnore
    private List<Album> results;

    @Override
    public void execute() {
        results = manager.requestUserAlbums(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ShowAlbumsOutput(this)).generateOutputNode();
    }
}
