package app.management;

import app.pages.ArtistPage;
import app.pages.HomePage;
import app.pages.HostPage;
import app.pages.LikedContentPage;
import app.pages.Page;
import app.player.entities.Player;
import app.player.entities.SearchBar;
import app.users.User;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public class UserInterface {
    private final User profile;
    private final SearchBar searchbar;
    private final Page homePage;
    private final Page likedContentPage;
    @Setter
    private Page currentPage;
    private final Player player;

    public UserInterface(final User user) {
        profile = user;
        searchbar = new SearchBar(user.getUsername());
        homePage = new HomePage(user);
        likedContentPage = new LikedContentPage(user);
        currentPage = homePage;
        player = new Player(user);
    }
}
