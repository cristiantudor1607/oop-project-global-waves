package globalwaves.commands.enums;


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

    private SearchType() {}

    /**
     * Method that parse a String and generates a Type enum associated.
     * @param typeAsString The String that should be parsed.
     * @return a Type enum if the String can be turned into an enum, or null otherwise.
     */
    public static Type parseString(final String typeAsString) {
        if (typeAsString.equals("song"))
            return Type.SONG;

        if (typeAsString.equals("podcast"))
            return Type.PODCAST;

        if (typeAsString.equals("playlist"))
            return Type.PLAYLIST;

        if (typeAsString.equals("album"))
            return Type.ALBUM;

        if (typeAsString.equals("artist"))
            return Type.ARTIST;

        if (typeAsString.equals("host"))
            return Type.HOST;

        return Type.UNKNOWN;
    }
}
