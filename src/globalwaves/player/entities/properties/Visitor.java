package globalwaves.player.entities.properties;

import globalwaves.player.entities.paging.ArtistPage;
import globalwaves.player.entities.paging.HomePage;
import globalwaves.player.entities.paging.HostPage;
import globalwaves.player.entities.paging.LikedContentPage;

public interface Visitor {
    String visit(HomePage page);
    //String visit(LikedContentPage page);
    String visit(ArtistPage page);
    //String visit(HostPage page);
}
