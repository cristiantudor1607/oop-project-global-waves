package app.enums;

public final class FilterType {
    public enum Type {
        NAME,
        ALBUM,
        TAGS,
        LYRICS,
        GENRE,
        RELEASE_YEAR,
        ARTIST,
        OWNER,
        DESCRIPTION,
        ARTIST_USERNAME,
        HOST_USERNAME,
        UNKNOWN,
    }

    private FilterType() { }

    /**
     * Converts the given string to a FilterType enum. If it doesn't recognize the type, it
     * returns the type UNKNOWN.
     * @param filterAsString The name of the type
     * @return The specific filter type, or unknown
     */
    public static Type parseString(final String filterAsString) {
        if (filterAsString.equals("name")) {
            return Type.NAME;
        }

        if (filterAsString.equals("album")) {
            return Type.ALBUM;
        }

        if (filterAsString.equals("tags")) {
            return Type.TAGS;
        }

        if (filterAsString.equals("lyrics")) {
            return Type.LYRICS;
        }

        if (filterAsString.equals("genre")) {
            return Type.GENRE;
        }

        if (filterAsString.equals("releaseYear")) {
            return Type.RELEASE_YEAR;
        }

        if (filterAsString.equals("artist")) {
            return Type.ARTIST;
        }

        if (filterAsString.equals("owner")) {
            return Type.OWNER;
        }

        if (filterAsString.equals("description")) {
            return Type.DESCRIPTION;
        }

        return Type.UNKNOWN;
    }

    /**
     * Converts the given string to a FilterType enum. It is designed to be used for "name" filter,
     * which have 3 different meanings: artist username, host username, or an entity name.
     * @param filterAsString The name of the type
     * @param type The type of search
     * @return The specific filter type, or {@code UNKNOWN}, if the filter isn't "name"
     */
    public static Type parseNameFilter(final String filterAsString, final SearchType.Type type) {
        if (type == SearchType.Type.ARTIST && filterAsString.equals("name")) {
            return Type.ARTIST_USERNAME;
        }

        if (type == SearchType.Type.HOST && filterAsString.equals("name")) {
            return Type.HOST_USERNAME;
        }

        if (filterAsString.equals("name")) {
            return Type.NAME;
        }

        return Type.UNKNOWN;
    }
}
