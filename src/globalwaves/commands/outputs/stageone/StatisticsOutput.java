package globalwaves.commands.outputs.stageone;

import globalwaves.commands.stageone.TopFivePlaylistsInterrogator;
import globalwaves.commands.stageone.TopFiveSongsInterrogator;
import globalwaves.commands.stagetwo.OnlineUsersInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

import java.util.List;

@Getter
public class StatisticsOutput extends CommandOutputFormatter {
    private final List<String> result;

    public StatisticsOutput(final TopFiveSongsInterrogator executedQuery) {
        command = "getTop5Songs";
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getResult();
    }

    public StatisticsOutput(final TopFivePlaylistsInterrogator executedQuery) {
        command = "getTop5Playlists";
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getResult();
    }

    public StatisticsOutput(final OnlineUsersInterrogator executedQuery) {
        command = "getOnlineUsers";
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getResults();
    }
}
