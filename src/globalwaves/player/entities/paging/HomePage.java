package globalwaves.player.entities.paging;

import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.User;
import globalwaves.player.entities.library.HelperTool;
import globalwaves.player.entities.properties.Visitor;
import globalwaves.player.entities.utilities.SortByNumberOfLikes;
import globalwaves.player.entities.utilities.SortByTotalLikes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePage extends Page {
    private final List<Song> likedSongs;
    private final List<Playlist> followingPlaylists;

    public HomePage(User user) {
        likedSongs = user.getLikes();
        followingPlaylists = user.getFollowing();
    }

    public List<Song> getRecommendedSongs() {
        // Make a shallow copy of the likedSongs list, because we want to sort
        // the songs, but don't disturb their order in User. That would happen
        // because likedSongs it's just a reference to the liked songs list from
        // User class
        List<Song> copy = new ArrayList<>(likedSongs);

        copy.sort(new SortByNumberOfLikes());
        HelperTool.getInstance().truncateResults(copy);
        return copy;
    }

    public List<Playlist> getRecommendedPlaylist() {
        // We make a shallow copy. The explanation is the same as in the getRecommendedSongs
        // case
        List<Playlist> copy = new ArrayList<>(followingPlaylists);

        copy.sort(new SortByTotalLikes());
        HelperTool.getInstance().truncateResults(copy);
        return copy;
    }

    @Override
    public String accept(Visitor v) {
        return v.visit(this);
    }
}
