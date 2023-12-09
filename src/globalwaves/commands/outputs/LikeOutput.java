package globalwaves.commands.outputs;

import globalwaves.commands.LikeInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class LikeOutput extends CommandOutputFormatter {
    private String message;

    public LikeOutput(LikeInterrogator executedQuery) {
        command = "like";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case LIKED -> message = "Like registered successfully.";
            case UNLIKED -> message = "Unlike registered successfully.";
            case NO_SOURCE -> message = "Please load a source before liking or unliking.";
            case NOT_A_SONG -> message = "Loaded source is not a song.";
        }
    }
}
