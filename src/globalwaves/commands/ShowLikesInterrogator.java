package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

import java.util.List;

@Getter
class ShowLikesOutput extends CommandOutputFormatter {
    private List<String> result;

    public ShowLikesOutput(ShowLikesInterrogator executedQuery) {
        command = "showPreferredSongs";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getSongNames();
    }
}

@Getter
public class ShowLikesInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> songNames;

    @Override
    public JsonNode execute(ActionManager manager) {
        songNames = manager.requestLikedSongs(this);

        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);
        return (new ShowLikesOutput(this)).generateOutputNode();
    }
}
