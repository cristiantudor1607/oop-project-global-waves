package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.enums.exitstats.stagetwo.AddEventExit;
import globalwaves.commands.stagetwo.AddEventInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddEventOutput extends CommandOutputFormatter {
    private final String message;

    public AddEventOutput(final AddEventInterrogator executedQuery) {
        command = "addEvent";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final AddEventExit.Status atExit) {
        return switch (atExit) {
            case SUCCESS -> username + " has added new event successfully.";
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case SAME_NAME -> username + " has another event with the same name";
            case NOT_AN_ARTIST -> username + " is not an artist.";
            case INVALID_DATE -> "Event for " + username + " does not have a valid date.";
        };
    }
}
