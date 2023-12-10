package globalwaves.commands.outputs.stageone;

import globalwaves.commands.stageone.ShowLikesInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowLikesOutput extends CommandOutputFormatter {
    private List<String> result;

    public ShowLikesOutput(ShowLikesInterrogator executedQuery) {
        command = "showPreferredSongs";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getSongNames();
    }
}
