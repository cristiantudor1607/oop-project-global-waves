package app.properties;


public interface Observable<T> {

    /**
     * Adds a new info to the internal state of the subject. The info is the data
     * that will be observed / sent by / to the observers.
     * @param info The data to be added
     */
    void insertInfo(final T info);

    /**
     * Getter for the generic info. It returns the internal state of the subject, which can
     * be anything. That's way this method should be implemented by all subjects.
     * @return The internal info of the subject / the internal state of the subject
     */
    T getInfo();

    /**
     * Attach a new observer to the subject.
     * @param observer The new observer to be attached
     */
    void attach(final Observer<T> observer);

    /**
     * Detach an observer from the subject.
     * @param observer The observer to be detached
     */
    void detach(final Observer<T> observer);

    /**
     * Notifies all the observers attached to the subject.
     */
    void notifyAllObservers();
}
