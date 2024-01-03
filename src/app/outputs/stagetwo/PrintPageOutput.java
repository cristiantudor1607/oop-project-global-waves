package app.outputs.stagetwo;

import app.commands.stagetwo.PrintPageInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class PrintPageOutput extends CommandOutputFormatter {
    private final String message;

    public PrintPageOutput(final PrintPageInterrogator executedQuery) {
        command = "printCurrentPage";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getOutput());
    }

    /**
     * Selects between the message sent by manager and the offline default message, in
     * order to generate the output for printCurrentPage command.
     * @param username The name of the user that gave the command
     * @param output The output message sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final String output) {
        if (output == null) {
            return "Unexpected: " + username + "is an artist or a host.\n";
        }

        return output;
    }
}
