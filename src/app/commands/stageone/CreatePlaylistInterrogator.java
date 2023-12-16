package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.CreationExit;
import app.outputs.stageone.CreatePlaylistOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class CreatePlaylistInterrogator extends CommandObject {
    private String playlistName;
    @JsonIgnore private CreationExit.Status exitStatus;


    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = CreationExit.Status.OFFLINE;
            return;
        }

        exitStatus = manager.requestPlaylistCreation(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new CreatePlaylistOutput(this)).generateOutputNode();
    }
}
