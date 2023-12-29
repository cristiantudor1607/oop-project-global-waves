package app.utilities;

import app.users.User;

import java.util.Comparator;

public class SortByArtistLikes implements Comparator<User> {
    /**
     * Compares the number of likes of the users. If the users aren't artists,
     * the compare will consider them equal.
     * @param o1 The first user to be compared
     * @param o2 The second user to be compared
     * @return A positive integer, if the first user has more likes, {@code 0}, if the
     * users have the same number of likes, or if at least one of them isn't
     * an artist, or a negative integer if the second one has more likes
     */
    @Override
    public int compare(final User o1, final User o2) {
        if (!o1.isArtist() || !o2.isArtist()) {
            return 0;
        }

        return o1.getNumberOfLikes() - o2.getNumberOfLikes();
    }
}
