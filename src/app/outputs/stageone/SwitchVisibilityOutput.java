package app.outputs.stageone;

import app.exitstats.stageone.SwitchVisibilityExit;
import app.commands.stageone.VisibilityInterrogator;
import app.utilities.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class SwitchVisibilityOutput extends CommandOutputFormatter {
    private final String message;

    public SwitchVisibilityOutput(final VisibilityInterrogator executedQuery) {
        command = "switchVisibility";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    /**
     * Generates a message for switchVisibility command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username,
                                  final SwitchVisibilityExit.Status atExit) {
        return switch (atExit) {
            case TOO_HIGH -> StringConstants.PLAYLIST_ID_HIGH;
            case MADE_PUBLIC -> StringConstants.MADE_PUBLIC;
            case MADE_PRIVATE -> StringConstants.MADE_PRIVATE;
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
