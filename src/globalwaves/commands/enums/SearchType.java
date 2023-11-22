package globalwaves.commands.enums;


public class SearchType {
    public enum type {
        SONG,
        PODCAST,
        PLAYLIST,
    }

    private SearchType() {};

    public static type ParseString(final String typeAsString) {
        if (typeAsString.equals("song"))
            return type.SONG;

        if (typeAsString.equals("podcast"))
            return type.PODCAST;

        if (typeAsString.equals("playlist"))
            return type.PLAYLIST;

        return null;
    }
}
