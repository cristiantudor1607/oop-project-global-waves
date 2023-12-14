package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.stagetwo.ChangePageInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
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
