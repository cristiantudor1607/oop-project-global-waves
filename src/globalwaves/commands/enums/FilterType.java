package globalwaves.commands.enums;

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
        USERNAME,
        ARTIST_USERNAME,
        HOST_USERNAME,
        UNKNOWN,
    }

    private FilterType() {}

    /**
     * Method that parse a String to get a FilterType.Type enum, designed as a utility class.
     * @param filterAsString The String to be converted.
     * @return A type, if the String matches a type, or null otherwise
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

        if (filterAsString.equals("username"))
            return Type.USERNAME;

        return Type.UNKNOWN;
    }

    public static Type parseString(final String filterAsString, final SearchType.Type type) {
        if (type == SearchType.Type.ARTIST && filterAsString.equals("name"))
            return Type.ARTIST_USERNAME;

        if (type == SearchType.Type.HOST && filterAsString.equals("name"))
            return Type.HOST_USERNAME;

        return Type.UNKNOWN;
    }
}
