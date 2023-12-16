package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.ShowLikesOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowLikesInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> songNames;

    @Override
    public void execute() {
        songNames = manager.requestLikedSongs(this);

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ShowLikesOutput(this)).generateOutputNode();
    }
}
