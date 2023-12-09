package globalwaves.commands.outputs;

import globalwaves.commands.PlayPauseInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class PlayPauseOutput extends CommandOutputFormatter {
    private String message;

    public PlayPauseOutput(final PlayPauseInterrogator executedQuery) {
        command = "playPause";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case NO_SOURCE -> message = "Please load a source before attempting to "
                    + "pause or resume playback.";
            case PAUSED -> message = "Playback paused successfully.";
            case RESUMED -> message = "Playback resumed successfully.";
            default -> {

            }
        }
    }
}
