package app.outputs.stageone;

import app.exitstats.stageone.SearchExit;
import app.commands.stageone.SearchInterrogator;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchOutput extends CommandOutputFormatter {
    private final String message;
    private final List<String> results;

    public SearchOutput(final SearchInterrogator executedQuery) {
        command = "search";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        results = executedQuery.getResults();
        message = generateMessage(user, results.size(), executedQuery.getExitStatus());
    }

    /**
     * Generates a message for createPlaylist command.
     * @param username The name of the user that gave the command
     * @param nr Number of results returned
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final int nr,
                                  final SearchExit.Status atExit) {
        if (atExit == SearchExit.Status.OFFLINE) {
            return username + StringConstants.OFFLINE_DESCRIPTOR;
        }

        return "Search returned " + nr + " results";
    }
}
