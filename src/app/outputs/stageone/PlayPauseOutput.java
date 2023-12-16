package app.outputs.stageone;

import app.exitstats.stageone.PlayPauseExit;
import app.commands.stageone.PlayPauseInterrogator;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class PlayPauseOutput extends CommandOutputFormatter {
    private final String message;

    public PlayPauseOutput(final PlayPauseInterrogator executedQuery) {
        command = "playPause";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final PlayPauseExit.Status atExit) {
        return switch (atExit) {
            case RESUMED -> StringConstants.RESUMED;
            case PAUSED -> StringConstants.PAUSED;
            case NO_SOURCE -> StringConstants.PLAY_PAUSE_NO_SRC;
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
