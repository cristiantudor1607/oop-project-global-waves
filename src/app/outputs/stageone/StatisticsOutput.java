package app.outputs.stageone;

import app.commands.stageone.TopFivePlaylistsInterrogator;
import app.commands.stageone.TopFiveSongsInterrogator;
import app.commands.stagetwo.AllUsersInterrogator;
import app.commands.stagetwo.OnlineUsersInterrogator;
import app.commands.stagetwo.TopFiveAlbumsInterrogator;
import app.commands.stagetwo.TopFiveArtistsInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
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
