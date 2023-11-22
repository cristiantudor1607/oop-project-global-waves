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

    public static FilterType.type ParseString(final String filterAsString) {
        if (filterAsString.equals("name"))
            return FilterType.type.NAME;

        if (filterAsString.equals("album"))
            return FilterType.type.ALBUM;

        if (filterAsString.equals("tags"))
            return FilterType.type.TAGS;

        if (filterAsString.equals("lyrics"))
            return FilterType.type.LYRICS;

        if (filterAsString.equals("genre"))
            return FilterType.type.GENRE;

        if (filterAsString.equals("releaseYear"))
            return FilterType.type.RELEASE_YEAR;

        if (filterAsString.equals("artist"))
            return FilterType.type.ARTIST;

        if (filterAsString.equals("owner"))
            return FilterType.type.OWNER;

        return null;
    }
}
