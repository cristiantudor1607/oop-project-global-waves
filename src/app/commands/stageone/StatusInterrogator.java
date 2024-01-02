package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.StatusOutput;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Player;
import lombok.Getter;

@Getter
public class StatusInterrogator extends CommandObject {
    @JsonIgnore private Player player;

    /**
     * Executes the status command.
     */
    @Override
    public void execute() {
        player = manager.getPlayerByUsername(username);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new StatusOutput(this)).generateOutputNode();
    }
}
