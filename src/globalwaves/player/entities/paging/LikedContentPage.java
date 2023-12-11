package globalwaves.player.entities.paging;

import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Player;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.User;
import globalwaves.player.entities.properties.ContentVisitor;

import java.util.List;

public class LikedContentPage extends Page {
    private final List<AudioFile> likedSongs;
    private final List<Playlist> followingPlaylists;

    public LikedContentPage(final User user) {
        likedSongs = user.getLikes();
        followingPlaylists = user.getFollowing();
   }

    @Override
    public void accept(ContentVisitor v) {

    }
}
