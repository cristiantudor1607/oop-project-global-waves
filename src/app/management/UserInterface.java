package app.management;

import app.pages.*;
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
    private final PageHistory pageHistory;
    private final Player player;

    public UserInterface(final User user) {
        profile = user;
        searchbar = new SearchBar(user.getUsername());
        homePage = new HomePage(user);
        likedContentPage = new LikedContentPage(user);
        player = new Player(user);
        pageHistory = new PageHistory();
        pageHistory.visitPage(homePage);
    }

    /**
     * Sets the current page to the given page.
     *
     * @param page The page to be visited
     */
    public void setPage(final Page page) {
        pageHistory.visitPage(page);
    }

    /**
     * Returns the current page of the user from the interface
     * .
     * @return The current page of the user.
     */
    public Page getCurrentPage() {
        return pageHistory.getCurrentPage();
    }
}
