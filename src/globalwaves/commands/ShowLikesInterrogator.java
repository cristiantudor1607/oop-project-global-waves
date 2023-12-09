package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.ShowLikesOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowLikesInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> songNames;

    @Override
    public JsonNode execute(ActionManager manager) {
        songNames = manager.requestLikedSongs(this);

        manager.setLastActionTime(timestamp);
        return (new ShowLikesOutput(this)).generateOutputNode();
    }
}
