package app.properties;

import app.pages.ArtistPage;
import app.pages.HomePage;
import app.pages.HostPage;
import app.pages.LikedContentPage;

public interface Visitor {
    String visit(HomePage page);
    String visit(LikedContentPage page);
    String visit(ArtistPage page);
    String visit(HostPage page);
}
