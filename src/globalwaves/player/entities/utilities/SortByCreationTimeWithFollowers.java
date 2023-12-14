package globalwaves.player.entities.utilities;

import globalwaves.player.entities.Playlist;

import java.util.Comparator;

public class SortByCreationTimeWithFollowers implements Comparator<Playlist> {
    @Override
    public int compare(Playlist o1, Playlist o2) {
        if (o1.getFollowersNumber() != o2.getFollowersNumber())
            return 0;

        return o1.getCreationTime() - o2.getCreationTime();
    }
}
