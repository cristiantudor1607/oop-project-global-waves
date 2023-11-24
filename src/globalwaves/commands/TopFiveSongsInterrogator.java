package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

import java.util.List;

@Getter
public class TopFiveSongsInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> result;

    @Override
    public JsonNode execute(ActionManager manager) {
        result = manager.requestTopFiveSongs();

        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new StatisticsOutput(this)).generateOutputNode();
    }
}
