package app.commands.stagethree;

import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class PageNavigationOutput extends CommandOutputFormatter {
    private final String message;

    public PageNavigationOutput(final PrevPageInterrogator executedQuery) {
        command = "previousPage";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }

    public PageNavigationOutput(final NextPageInterrogator executedQuery) {
        command = "nextPage";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getMessage();
    }
}
