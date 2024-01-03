package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stagetwo.ShowAlbumsOutput;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Album;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ShowAlbumsInterrogator extends CommandObject {
    @JsonIgnore
    private List<Album> results;

    /**
     * Executes the showAlbums command.
     */
    @Override
    public void execute() {
        results = manager.requestUserAlbums(username);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new ShowAlbumsOutput(this)).generateOutputNode();
    }
}
