package app.enums;


public final class SearchType {
    public enum Type {
        SONG,
        PODCAST,
        PLAYLIST,
        ALBUM,
        ARTIST,
        HOST,
        UNKNOWN,
    }

    private SearchType() { }

    /**
     * Converts the string given as parameter to a PageType enum. If it doesn't recognize the type
     * specified, it returns the type UNKNOWN.
     * @param typeAsString The name of the type
     * @return The specific search type, or unknown
     */
    public static Type parseString(final String typeAsString) {
        if (typeAsString.equals("song")) {
            return Type.SONG;
        }

        if (typeAsString.equals("podcast")) {
            return Type.PODCAST;
        }

        if (typeAsString.equals("playlist")) {
            return Type.PLAYLIST;
        }

        if (typeAsString.equals("album")) {
            return Type.ALBUM;
        }

        if (typeAsString.equals("artist")) {
            return Type.ARTIST;
        }

        if (typeAsString.equals("host")) {
            return Type.HOST;
        }

        return Type.UNKNOWN;
    }
}
