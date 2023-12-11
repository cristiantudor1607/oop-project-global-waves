package globalwaves.commands.outputs.stageone;

import globalwaves.commands.enums.exitstats.stageone.LoadExit;
import globalwaves.commands.stageone.LoadInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
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

    public String generateMessage(final String username, final LoadExit.Status atExit) {
        return switch (atExit) {
            case LOADED -> StringConstants.LOAD_SUCCESS;
            case EMPTY_SOURCE -> StringConstants.LOAD_EMPTY_SRC;
            case NO_SOURCE_SELECTED -> StringConstants.LOAD_NO_SELECT;
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }

}
