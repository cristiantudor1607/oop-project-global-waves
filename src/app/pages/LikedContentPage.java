package app.pages;

import app.player.entities.Playlist;
import app.player.entities.Song;
import app.properties.Visitor;
import app.users.User;
import lombok.Getter;

import java.util.List;

@Getter
public class LikedContentPage extends Page {
    private final List<Song> likedSongs;
    private final List<Playlist> followingPlaylists;

    public LikedContentPage(final User user) {
        likedSongs = user.getLikes();
        followingPlaylists = user.getFollowing();
   }

    /**
     * Accept method for visitors that returns a string.
     * @param v The visitor
     * @return A string. It depends on the visitor what string contains.
     */
    @Override
    public String accept(final Visitor v) {
        return v.visit(this);
    }
}
