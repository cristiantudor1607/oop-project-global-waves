package app.enums;

import lombok.NonNull;

public class PageType {
    public enum Type {
        HOME,
        LIKED,
        UNKNOWN,
    }

    private PageType() { }

    public static Type parseString(@NonNull final String pageAsString) {
        if (pageAsString.equals("Home"))
            return Type.HOME;

        if (pageAsString.equals("LikedContent"))
            return Type.LIKED;

        return Type.UNKNOWN;
    }


}
