package globalwaves.player.entities.paging;

import globalwaves.player.entities.*;
import globalwaves.player.entities.properties.Visitor;

import java.util.List;

public class LikedContentPage extends Page {
    private final List<Song> likedSongs;
    private final List<Playlist> followingPlaylists;

    public LikedContentPage(final User user) {
        likedSongs = user.getLikes();
        followingPlaylists = user.getFollowing();
   }

    @Override
    public String accept(Visitor v) {
        return null;
    }
}
