package globalwaves.commands.outputs;

import globalwaves.commands.SearchInterrogator;
import globalwaves.commands.search.utils.EngineResultsParser;
import globalwaves.parser.templates.CommandOutputFormatter;
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
        results = EngineResultsParser.getNamesFromList(executedQuery.getSearchResults());
    }

    String generateMessage(final SearchInterrogator executingCommand) {
        int n = executingCommand.getSearchResults().size();
        return "Search returned " + n + " results";
    }
}
