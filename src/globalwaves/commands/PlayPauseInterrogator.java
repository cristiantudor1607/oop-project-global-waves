package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.PlayPauseExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class PlayPauseOutput extends CommandOutputFormatter {
    private String message;

    public PlayPauseOutput(PlayPauseInterrogator executedQuery) {
        command = "playPause";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case NO_SOURCE -> message = "Please load a source before attempting to " +
                    "pause or resume playback.";
            case PAUSED -> message = "Playback paused successfully.";
            case RESUMED -> message = "Playback resumed successfully.";
        }
    }
}

@Getter
public class PlayPauseInterrogator extends CommandObject {
    @JsonIgnore private PlayPauseExit.code exitCode;

    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Changing player state ...");

        exitCode = manager.requestUpdateState(this);
        manager.setLastActionTime(timestamp);
        return (new PlayPauseOutput(this)).generateOutputNode();
    }
}
