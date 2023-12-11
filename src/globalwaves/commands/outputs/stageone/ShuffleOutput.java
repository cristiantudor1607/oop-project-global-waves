package globalwaves.commands.outputs.stageone;

import globalwaves.commands.enums.exitstats.stageone.ShuffleExit;
import globalwaves.commands.stageone.ShuffleInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
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

    public String generateMessage(final String username, final ShuffleExit.Status atExit) {
        return switch (atExit) {
            case ACTIVATED -> StringConstants.SHUFFLE_ON;
            case DEACTIVATED -> StringConstants.SHUFFLE_OFF;
            case NOT_A_PLAYLIST -> StringConstants.SHUFFLE_NOT_PLAYLIST;
            case NO_SOURCE_LOADED -> StringConstants.SHUFFLE_NO_SRC;
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
