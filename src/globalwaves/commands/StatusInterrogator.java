package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.StatusOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.Player;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class StatusInterrogator extends CommandObject {
    @JsonIgnore private Player requestedPlayer;

    /**
     * The method executes the Status Command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public JsonNode execute(final ActionManager manager) {
        requestedPlayer = manager.requestPlayer(this);

        manager.setLastActionTime(timestamp);
        return (new StatusOutput(this)).generateOutputNode();
    }
}
