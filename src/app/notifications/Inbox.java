package app.notifications;

import app.management.IDContainer;
import app.properties.Observable;
import app.properties.Observer;
import app.users.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Inbox implements Observer<Notification> {
    private final List<Notification> notifications;
    private final int id;

    public Inbox() {
        notifications = new ArrayList<>();
        id = IDContainer.getInstance().useInboxId();
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

    // TODO: Add doc
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inbox inbox)) return false;
        return getId() == inbox.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
