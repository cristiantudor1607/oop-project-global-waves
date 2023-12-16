package app.enums;

public class UserType {
    public enum Type {
        USER,
        ARTIST,
        HOST,
        ADMIN,
        UNKNOWN,
    }

    private UserType() {}

    public static Type parseString(final String type) {
        if (type.equals("user"))
            return Type.USER;

        if (type.equals("artist"))
            return Type.ARTIST;

        if (type.equals("host"))
            return Type.HOST;

        return Type.UNKNOWN;
    }
}
