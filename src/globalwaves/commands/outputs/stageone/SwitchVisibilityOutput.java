package globalwaves.commands.outputs.stageone;

import globalwaves.commands.enums.exitstats.stageone.SwitchVisibilityExit;
import globalwaves.commands.stageone.VisibilityInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class SwitchVisibilityOutput extends CommandOutputFormatter {
    private String message;

    public SwitchVisibilityOutput(VisibilityInterrogator executedQuery) {
        command = "switchVisibility";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

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
