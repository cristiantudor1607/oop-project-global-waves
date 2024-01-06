package app.statistics;

import app.properties.NamePossessor;

import java.util.Objects;

public record Genre(String genre) implements NamePossessor {

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
    public boolean equals(final Object o) {
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
