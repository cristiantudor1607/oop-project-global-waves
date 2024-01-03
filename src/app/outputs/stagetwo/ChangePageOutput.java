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
        message = generateMessage(user, executedQuery.getOutput());
    }

    /**
     * Selects between the message sent by manager and the offline default message, in
     * order to generate the output for changePage command.
     * @param username The name of the user that gave the command
     * @param output The output message sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final String output) {
        if (output == null) {
            return username + StringConstants.OFFLINE_DESCRIPTOR;
        }

        return output;
    }
}
