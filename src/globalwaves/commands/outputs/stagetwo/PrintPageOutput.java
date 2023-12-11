package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.stagetwo.PrintPageInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class PrintPageOutput extends CommandOutputFormatter {
    private final String message;

    public PrintPageOutput(final PrintPageInterrogator execQuery) {
        command = "printCurrentPage";
        user = execQuery.getUsername();
        timestamp = execQuery.getTimestamp();
        message = generateMessage(execQuery);
    }

    public String generateMessage(final PrintPageInterrogator execQuery) {
        String output = execQuery.getOutput();
        String username = execQuery.getUsername();

        if (output == null)
            return username + StringConstants.OFFLINE_DESCRIPTOR;

        return output;
    }
}
