package app.utilities;

import app.player.entities.Playlist;

import java.util.Comparator;

public class SortByFollowers implements Comparator<Playlist> {
    @Override
    public int compare(Playlist o1, Playlist o2) {
        return o2.getFollowersNumber() - o1.getFollowersNumber();
    }
}
