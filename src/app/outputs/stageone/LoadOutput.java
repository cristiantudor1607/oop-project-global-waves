package app.outputs.stageone;

import app.exitstats.stageone.LoadExit;
import app.commands.stageone.LoadInterrogator;
import app.utilities.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class LoadOutput extends CommandOutputFormatter {
    private final String message;

    public LoadOutput(final LoadInterrogator executedLoad) {
        command = "load";
        user = executedLoad.getUsername();
        timestamp = executedLoad.getTimestamp();
        message = generateMessage(user, executedLoad.getExitStatus());
    }

    /**
     * Generates a message for load command.
     * @param username The name of the user that gave the command
     * @param atExit The exit code sent by manager
     * @return A specific message
     */
    public String generateMessage(final String username, final LoadExit.Status atExit) {
        return switch (atExit) {
            case LOADED -> StringConstants.LOAD_SUCCESS;
            case EMPTY_SOURCE -> StringConstants.LOAD_EMPTY_SRC;
            case NO_SOURCE_SELECTED -> StringConstants.LOAD_NO_SELECT;
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }

}
