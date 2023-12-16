package app.outputs.stagetwo;

import app.exitstats.stagetwo.RemoveAnnouncementExit;
import app.commands.stagetwo.RemoveAnnouncementInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class RemoveAnnouncementOutput extends CommandOutputFormatter {
    private String message;

    public RemoveAnnouncementOutput(final RemoveAnnouncementInterrogator executedQuery) {
        command = "removeAnnouncement";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final RemoveAnnouncementExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + "doesn't exist.";
            case NOT_HOST -> username + " is not a host.";
            case INVALID_NAME -> username + " has no announcement with the given name.";
            case SUCCESS -> username + " has successfully deleted the announcement.";
        };
    }

}
