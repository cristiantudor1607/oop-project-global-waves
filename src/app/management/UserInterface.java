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

    /**
     * Checks if the user is currently on an artist page.
     * @return {@code true}, if the user is on an artist page, {@code false} otherwise
     */
    public boolean isOnArtistPage() {
        String username = currentPage.getArtistName();
        return username != null;
    }

    /**
     * Sets the currentPage to Home
     */
    public void goToHomePage() {
        currentPage = homePage;
    }

    /**
     * Sets the current page to LikedContentPage
     */
    public void goToLikedContentPage() {
        currentPage = likedContentPage;
    }

    /**
     * Sets the artist page as current page
     * @param artistPage Any page of an artist that exists
     */
    public void goToArtistPage(@NonNull final ArtistPage artistPage) {
        currentPage = artistPage;
    }

    /**
     * Sets the host page as current page
     * @param hostPage Any page of a host that exists
     */
    public void goToHostPage(@NonNull final HostPage hostPage) {
        currentPage = hostPage;
    }
}
