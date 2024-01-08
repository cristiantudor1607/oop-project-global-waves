package app.properties;

public interface Observer<T> {
    /**
     * Method called by the subject to notify the observer. The observer retrieves the data
     * directly from the subject, through getters. It must be implemented by all observers.
     * @param subject The subject that notifies {@code this} observer
     */
    void update(final Observable<T> subject);
}
