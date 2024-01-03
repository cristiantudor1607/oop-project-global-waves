package app.outputs.stageone;

import app.exitstats.stageone.ShuffleExit;
import app.commands.stageone.ShuffleInterrogator;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class ShuffleOutput extends CommandOutputFormatter {
    private String message;

    public ShuffleOutput(final ShuffleInterrogator executedQuery) {
        command = "shuffle";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    /**
     * Generates a message for shuffle command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final ShuffleExit.Status atExit) {
        return switch (atExit) {
            case ACTIVATED -> StringConstants.SHUFFLE_ON;
            case DEACTIVATED -> StringConstants.SHUFFLE_OFF;
            case NOT_A_PLAYLIST -> StringConstants.SHUFFLE_NOT_COLLECTION;
            case NO_SOURCE_LOADED -> StringConstants.SHUFFLE_NO_SRC;
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
