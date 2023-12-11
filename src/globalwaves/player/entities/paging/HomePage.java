package globalwaves.player.entities.paging;

import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.User;
import globalwaves.player.entities.properties.ContentVisitor;

import java.util.List;

public class HomePage extends Page {
    private final List<AudioFile> likedSongs;
    private final List<Playlist> followingPlaylists;

    public HomePage(User user) {
        likedSongs = user.getLikes();
        followingPlaylists = user.getFollowing();
    }

    @Override
    public void accept(ContentVisitor v) {

    }
}
