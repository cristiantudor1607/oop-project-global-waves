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
        message = generateMessage(executedQuery);
        results = executedQuery.getResults();
    }

    public String generateMessage(final SearchInterrogator executedQuery) {
        if (executedQuery.getExitStatus() == SearchExit.Status.OFFLINE)
            return executedQuery.getUsername() + StringConstants.OFFLINE_DESCRIPTOR;

        int n = executedQuery.getResults().size();
        return "Search returned " + n + " results";
    }
}
