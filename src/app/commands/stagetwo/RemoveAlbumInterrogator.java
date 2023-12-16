package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.RemoveAlbumExit;
import app.outputs.stagetwo.RemoveAlbumOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class RemoveAlbumInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private RemoveAlbumExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestRemovingAlbum(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new RemoveAlbumOutput(this)).generateOutputNode();
    }
}
