package app.notifications;

import app.properties.Observable;
import app.properties.Observer;

import java.util.ArrayList;
import java.util.List;

public class Notifier implements Observable<Notification> {
    private final List<Observer<Notification>> observers;
    private Notification notification;

    public Notifier() {
        observers = new ArrayList<>();
    }

    /**
     * Adds a new notification to the internal state of the {@code Notifier}.
     * It changes the {@code notification} field of {@code this} notifier.
     * @param info The notification to be added
     */
    @Override
    public void insertInfo(final Notification info) {
        this.notification = info;
        notifyAllObservers();
    }

    /**
     * Getter for the {@code notification} field. This method is called by the observer to
     * retrieve the data from {@code this} subject.
     * @return The last notification added
     */
    @Override
    public Notification getInfo() {
        return notification;
    }

    /**
     * Attach a new observer to the subject.
     *
     * @param observer The new observer to be attached
     */
    @Override
    public void attach(final Observer<Notification> observer) {
        observers.add(observer);
    }

    /**
     * Detach an observer from the subject.
     *
     * @param observer The observer to be detached
     */
    @Override
    public void detach(final Observer<Notification> observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all the observers attached to the subject.
     * It sends the
     */
    @Override
    public void notifyAllObservers() {
        for (Observer<Notification> observer: observers) {
            observer.update(this);
        }
    }
}
