package app.pages.features;

import java.util.Objects;

public record Announcement(String name, String description) {


    /**
     * Compares this announcement with the specified object. The result is true if and only if
     * the argument is not null and is an Announcement object that represents the same announcement
     * as this object.
     *
     * @param o The object to compare this announcement against
     * @return {@code true}, if the given object represents the same announcement as this
     * announcement, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Announcement)) {
            return false;
        }

        Announcement announcement = (Announcement) o;
        return name.equals(announcement.name())
                && description.equals(announcement.description());
    }

    /**
     * Returns a hashcode value for this announcement. If two objects are equal according to the
     * equals method, then calling the hashCode method on each of the two objects must produce the
     * same integer result.
     *
     * @return A hashcode value for this announcement
     */
    @Override
    public int hashCode() {
        return Objects.hash(name(), description());
    }
}
