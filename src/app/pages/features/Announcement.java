package app.pages.features;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Announcement {
    private final String name;
    private final String description;

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Announcement)) return false;
        Announcement announcement = (Announcement) o;
        return name.equals(announcement.getName()) &&
                description.equals(announcement.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }
}
