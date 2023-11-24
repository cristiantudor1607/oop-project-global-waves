package globalwaves.commands;

import globalwaves.parser.commands.CommandOutputFormatter;
import lombok.Getter;

import java.util.List;

@Getter
public class StatisticsOutput extends CommandOutputFormatter {
    private final List<String> result;

    public StatisticsOutput(TopFiveSongsInterrogator executedQuery) {
        command = "getTop5Songs";
        user = null;
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getResult();
    }

    public StatisticsOutput(TopFivePlaylistsInterrogator executedQuery) {
        command = "getTop5Playlists";
        user = null;
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getResult();
    }
}
