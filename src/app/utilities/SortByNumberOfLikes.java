package app.utilities;

import app.player.entities.Song;

import java.util.Comparator;

public class SortByNumberOfLikes implements Comparator<Song> {
    /**
     * Compares the number of likes of the two songs.
     * @param o1 The first song to be compared.
     * @param o2 The second song to be compared.
     * @return A positive integer, if the first one has more likes, {@code 0}, if they
     * have the same number of likes, or a negative integer, if the second one has more
     * likes
     */
    @Override
    public int compare(final Song o1, final Song o2) {
        return o1.getLikesNumber() - o2.getLikesNumber();
    }
}
