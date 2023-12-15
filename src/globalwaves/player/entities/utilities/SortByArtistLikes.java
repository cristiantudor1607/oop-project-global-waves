package globalwaves.player.entities.utilities;

import globalwaves.player.entities.User;

import java.util.Comparator;

public class SortByArtistLikes implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        return o2.getNumberOfLikes() - o1.getNumberOfLikes();
    }
}