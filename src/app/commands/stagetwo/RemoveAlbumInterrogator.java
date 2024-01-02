package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.RemoveAlbumExit;
import app.outputs.stagetwo.RemoveAlbumOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RemoveAlbumInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private RemoveAlbumExit.Status exitStatus;

    /**
     * Executes the removeAlbum command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestRemovingAlbum(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new RemoveAlbumOutput(this)).generateOutputNode();
    }
}
