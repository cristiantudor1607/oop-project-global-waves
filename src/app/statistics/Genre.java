package app.statistics;

import app.properties.NamedObject;

import java.util.Objects;

public record Genre(String genre) implements NamedObject {

    /**
     * Returns the name given to object
     *
     * @return The name of the object
     */
    @Override
    public String getName() {
        return genre;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Genre genre1)) {
            return false;
        }
        return Objects.equals(genre, genre1.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genre);
    }
}
