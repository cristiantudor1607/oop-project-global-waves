package app.pages;

import app.player.entities.*;
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

    @Override
    public String accept(Visitor v) {
        return v.visit(this);
    }
}
