package app.utilities;

import app.player.entities.Playlist;

import java.util.Comparator;

public class SortByFollowers implements Comparator<Playlist> {
    /**
     * Compares the followers of the two playlists.
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return A positive integer, if the first one has more followers, {@code 0}, if they
     * have the same number of followers, a negative integer, if the second one has more
     * followers
     */
    @Override
    public int compare(final Playlist o1, final Playlist o2) {
        return o1.getFollowersNumber() - o2.getFollowersNumber();
    }
}
