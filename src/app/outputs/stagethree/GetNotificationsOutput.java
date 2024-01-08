package app.outputs.stagethree;

import app.commands.stagethree.GetNotificationsInterrogator;
import app.notifications.Notification;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

import java.util.List;

@Getter
public class GetNotificationsOutput extends CommandOutputFormatter {
    private final List<Notification> notifications;

    public GetNotificationsOutput(final GetNotificationsInterrogator executedQuery) {
        command = "getNotifications";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        notifications = executedQuery.getNotifications();
    }
}
