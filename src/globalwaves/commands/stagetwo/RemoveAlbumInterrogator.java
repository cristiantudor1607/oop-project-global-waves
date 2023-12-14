package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.RemoveAlbumExit;
import globalwaves.commands.outputs.stagetwo.RemoveAlbumOutput;
import globalwaves.parser.templates.CommandObject;
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
