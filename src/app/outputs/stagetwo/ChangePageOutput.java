package app.outputs.stagetwo;

import app.commands.stagetwo.ChangePageInterrogator;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class ChangePageOutput extends CommandOutputFormatter {
    private final String message;

    public ChangePageOutput(final ChangePageInterrogator executedQuery) {
        command = "changePage";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = selectMessage(user, executedQuery.getOutput());
    }

    public String selectMessage(final String username, final String output) {
        if (output == null)
            return username + StringConstants.OFFLINE_DESCRIPTOR;

        return output;
    }
}
