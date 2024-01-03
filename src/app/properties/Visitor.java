package app.properties;

import app.pages.ArtistPage;
import app.pages.HomePage;
import app.pages.HostPage;
import app.pages.LikedContentPage;

public interface Visitor {
    /**
     * Visits a {@code HomePage} and returns its content.
     * @param page The page to be visited
     * @return A string containing the page content
     */
    String visit(HomePage page);

    /**
     * Visits a {@code LikedContentPage} and returns its content.
     * @param page The page to be visited
     * @return A string containing the page content
     */
    String visit(LikedContentPage page);

    /**
     * Visits an {@code ArtistPage} and returns its content.
     * @param page The page to be visited
     * @return A string containing the page content
     */
    String visit(ArtistPage page);

    /**
     * Visits a {@code HostPage} and returns its content.
     * @param page The page to be visited
     * @return A string containing the page content
     */
    String visit(HostPage page);
}
