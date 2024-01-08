package app.notifications;

import app.properties.Observable;
import app.properties.Observer;

import java.util.ArrayList;
import java.util.List;

public class Inbox implements Observer<Notification> {
    private final List<Notification> notifications;

    public Inbox() {
        notifications = new ArrayList<>();
    }

    /**
     * Returns the last notifications and clears the list.
     * @return A list containing the last notifications from {@code this} inbox
     */
    public List<Notification> readNotifications() {
        List<Notification> unread = new ArrayList<>(notifications);
        notifications.clear();
        return unread;
    }

    /**
     * Method called by subject to notify the observer. The observer takes the notification
     * directly from subject, through getters
     * @param subject The subject that notifies {@code this} observer
     */
    @Override
    public void update(final Observable<Notification> subject) {
        notifications.add(subject.getInfo());
    }

}
