package globalwaves.commands.outputs.stageone;

import globalwaves.commands.enums.exitstats.stageone.LikeExit;
import globalwaves.commands.stageone.LikeInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class LikeOutput extends CommandOutputFormatter {
    private String message;

    public LikeOutput(LikeInterrogator executedQuery) {
        command = "like";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(executedQuery.getUsername(), executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final LikeExit.Status atExit) {
        return switch (atExit) {
            case LIKED ->  "Like registered successfully.";
            case UNLIKED -> "Unlike registered successfully.";
            case NO_SOURCE -> "Please load a source before liking or unliking.";
            case NOT_A_SONG -> "Loaded source is not a song.";
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
