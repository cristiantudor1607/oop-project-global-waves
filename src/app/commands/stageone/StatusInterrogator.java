package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.StatusOutput;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Player;
import lombok.Getter;

@Getter
public class StatusInterrogator extends CommandObject {
    @JsonIgnore private Player requestedPlayer;


    @Override
    public void execute() {
        requestedPlayer = manager.requestPlayer(this);

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new StatusOutput(this)).generateOutputNode();
    }
}
