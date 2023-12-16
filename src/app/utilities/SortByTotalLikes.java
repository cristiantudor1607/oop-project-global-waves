package app.utilities;

import app.player.entities.Playlist;

import java.util.Comparator;

public class SortByTotalLikes implements Comparator<Playlist> {
    /**
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(Playlist o1, Playlist o2) {
        return o2.getTotalLikesNumber() - o1.getTotalLikesNumber();
    }
}
