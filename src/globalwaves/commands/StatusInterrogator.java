package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.Player;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class StatusOutput extends CommandOutputFormatter {
   PlayerStats stats;

    public StatusOutput(StatusInterrogator execQuery) {
        command = "status";
        user = execQuery.getUsername();
        timestamp = execQuery.getTimestamp();
        stats = new PlayerStats(execQuery.getRequestedPlayer());
    }
}

@Getter
public class StatusInterrogator extends CommandObject {
    @JsonIgnore Player requestedPlayer;

    @Override
    public JsonNode execute(ActionManager manager) {
        requestedPlayer = manager.requestPlayer(this);

        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new StatusOutput(this)).generateOutputNode();
    }
}
