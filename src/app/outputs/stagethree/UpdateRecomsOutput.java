package app.outputs.stagethree;

import app.commands.stagethree.UpdateRecomsInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class UpdateRecomsOutput extends CommandOutputFormatter {
    private final String message;

    public UpdateRecomsOutput(final UpdateRecomsInterrogator executedQuery) {
        command = "updateRecommendations";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    private String generateMessage(final String username,
                                   final UpdateRecomsInterrogator.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_NORMAL_USER -> username + " is not a normal user.";
            case NO_RECOMS -> "No new recommendations were found";
            case SUCCESS -> "The recommendations for user " + username +
                    " have been updated successfully.";
        };
    }
}
