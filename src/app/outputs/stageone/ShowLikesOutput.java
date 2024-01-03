package app.outputs.stageone;

import app.commands.stageone.ShowLikesInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowLikesOutput extends CommandOutputFormatter {
    private List<String> result;

    public ShowLikesOutput(final ShowLikesInterrogator executedQuery) {
        command = "showPreferredSongs";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getSongNames();
    }
}
