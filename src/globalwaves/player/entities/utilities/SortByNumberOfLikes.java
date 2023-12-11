package globalwaves.player.entities.utilities;

import globalwaves.player.entities.Song;

import java.util.Comparator;

public class SortByNumberOfLikes implements Comparator<Song> {
    @Override
    public int compare(Song o1, Song o2) {
        return o1.getLikesNumber() - o2.getLikesNumber();
    }
}
