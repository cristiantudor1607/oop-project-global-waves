package app.enums;

import lombok.NonNull;

public final class PageType {
    public enum Type {
        HOME,
        LIKED,
        ARTIST,
        HOST,
        UNKNOWN,
    }

    private PageType() { }

    /**
     * Converts the string given as parameter to a PageType enum. If it doesn't recognize the type
     * specified, it returns the type UNKNOWN.
     * @param pageAsString The name of the type
     * @return The specific page type, or unknown
     */
    public static Type parseString(@NonNull final String pageAsString) {
        if (pageAsString.equals("Home")) {
            return Type.HOME;
        }

        if (pageAsString.equals("LikedContent")) {
            return Type.LIKED;
        }

        if (pageAsString.equals("Artist")) {
            return Type.ARTIST;
        }

        if (pageAsString.equals("Host")) {
            return Type.HOST;
        }

        return Type.UNKNOWN;
    }
}
