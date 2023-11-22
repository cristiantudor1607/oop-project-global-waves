package globalwaves.commands.enums;

public class FilterType {
    public enum type {
        NAME,
        ALBUM,
        TAGS,
        LYRICS,
        GENRE,
        RELEASE_YEAR,
        ARTIST,
        OWNER
    }

    private FilterType() {}

    public static type ParseString(final String filterAsString) {
        if (filterAsString.equals("name"))
            return type.NAME;

        if (filterAsString.equals("album"))
            return type.ALBUM;

        if (filterAsString.equals("tags"))
            return type.TAGS;

        if (filterAsString.equals("lyrics"))
            return type.LYRICS;

        if (filterAsString.equals("genre"))
            return type.GENRE;

        if (filterAsString.equals("releaseYear"))
            return type.RELEASE_YEAR;

        if (filterAsString.equals("artist"))
            return type.ARTIST;

        if (filterAsString.equals("owner"))
            return type.OWNER;

        return null;
    }
}
