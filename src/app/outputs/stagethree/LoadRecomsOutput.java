package app.outputs.stagethree;

import app.commands.stagethree.LoadRecomsInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class LoadRecomsOutput extends CommandOutputFormatter {
    private final String message;

    public LoadRecomsOutput(final LoadRecomsInterrogator executedQuery) {
        command = "loadRecommendations";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    private String generateMessage(final String username,
                                   final LoadRecomsInterrogator.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case OFFLINE -> username + " is offline.";
            case NO_RECOMS -> "No recommendations available.";
            case SUCCESS -> "Playback loaded successfully.";
        };
    }
}
