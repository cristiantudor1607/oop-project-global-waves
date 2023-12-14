package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.enums.exitstats.stagetwo.AddAnnouncementExit;
import globalwaves.commands.stagetwo.AddAnnouncementInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddAnnouncementOutput extends CommandOutputFormatter {
    private final String message;

    public AddAnnouncementOutput(final AddAnnouncementInterrogator executedQuery) {
        command = "addAnnouncement";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }


    public String generateMessage(final String username, final AddAnnouncementExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_HOST -> username + " is not a host.";
            case SAME_NAME -> username + " has already added an announcement with this name.";
            case SUCCESS -> username + " has successfully added new announcement.";
        };
    }
}
