package app.utilities;

import app.player.entities.Playlist;

import java.util.Comparator;

public class SortByPlaylistLikes implements Comparator<Playlist> {
    /**
     * Compares the number of likes of the 2 playlists.
     * @param o1 The first playlist to be compared
     * @param o2 The second playlist to be compared
     * @return A positive integer, if the first one has more likes, {@code 0},
     * if they have the same number of likes, or a negative integer, if the second
     * one has more likes
     */
    @Override
    public int compare(Playlist o1, Playlist o2) {
        return o1.getTotalLikesNumber() - o2.getTotalLikesNumber();
    }
}
