package globalwaves.commands.outputs.stageone;

import globalwaves.commands.stageone.TopFivePlaylistsInterrogator;
import globalwaves.commands.stageone.TopFiveSongsInterrogator;
import globalwaves.commands.stagetwo.AllUsersInterrogator;
import globalwaves.commands.stagetwo.OnlineUsersInterrogator;
import globalwaves.commands.stagetwo.TopFiveAlbumsInterrogator;
import globalwaves.commands.stagetwo.TopFiveArtistsInterrogator;
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

    public StatisticsOutput(final AllUsersInterrogator executedQuery) {
        command = "getAllUsers";
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getResults();
    }

    public StatisticsOutput(final TopFiveAlbumsInterrogator executedQuery) {
        command = "getTop5Albums";
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getResults();
    }

    public StatisticsOutput(final TopFiveArtistsInterrogator executedQuery) {
        command = "getTop5Artists";
        timestamp = executedQuery.getTimestamp();
        result = executedQuery.getResults();
    }
}
