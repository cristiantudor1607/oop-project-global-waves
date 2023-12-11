package globalwaves.player.entities;

import globalwaves.player.entities.paging.*;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class UserInterface {
    private final User profile;
    private final SearchBar searchbar;
    private final Page homePage;
    private final Page likedContentPage;
    private Page currentPage;
    private final Player player;

    public UserInterface(final User user) {
        profile = user;
        searchbar = new SearchBar(user.getUsername());
        homePage = new HomePage(user);
        likedContentPage = new LikedContentPage(user);
        currentPage = homePage;
        player = new Player();
    }

    public void goToHomePage() {
        currentPage = homePage;
    }

    public void goToLikedContentPage() {
        currentPage = likedContentPage;
    }

    public void goToArtistPage(@NonNull final ArtistPage artistPage) {
        currentPage = artistPage;
    }

    public void goToHostPage(@NonNull final HostPage hostPage) {
        currentPage = hostPage;
    }
}
