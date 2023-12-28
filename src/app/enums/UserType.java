package app.enums;

public final class UserType {
    public enum Type {
        USER,
        ARTIST,
        HOST,
        ADMIN,
        UNKNOWN,
    }

    private UserType() { }

    /**
     * Converts the string given as parameter to a UserType enum. If it doesn't recognize the type
     * specified, it returns the type UNKNOWN.
     * @param typeAsString The name of the type
     * @return The specific user type, or unknown
     */
    public static Type parseString(final String typeAsString) {
        if (typeAsString.equals("user")) {
            return Type.USER;
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
