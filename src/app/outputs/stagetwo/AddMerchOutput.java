package app.outputs.stagetwo;

import app.exitstats.stagetwo.AddMerchExit;
import app.commands.stagetwo.AddMerchInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddMerchOutput extends CommandOutputFormatter {
    private final String message;

    public AddMerchOutput(final AddMerchInterrogator executedQuery) {
        command = "addMerch";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final AddMerchExit.Status atExit) {
        return switch (atExit) {
            case SUCCESS -> username + " has added new merchandise successfully.";
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_ARTIST -> username + " is not an artist.";
            case SAME_NAME -> username + " has merchandise with the same name.";
            case NEGATIVE_PRICE -> "Price for merchandise can not be negative.";
        };
    }
}
