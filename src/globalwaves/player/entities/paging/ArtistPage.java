package globalwaves.player.entities.paging;

import globalwaves.player.entities.Artist;
import globalwaves.player.entities.Event;
import globalwaves.player.entities.User;
import globalwaves.player.entities.properties.Visitor;

import java.util.ArrayList;
import java.util.List;

public class ArtistPage extends Page {
    private final User artist;
    private final List<Event> events;

    public ArtistPage(final User artist) {
        this.artist = artist;
        events = new ArrayList<>();
    }

    @Override
    public String accept(Visitor v) {
        return null;
    }

    @Override
    public String getArtistName() {
        return artist.getUsername();
    }

    @Override
    public String getUsername() {
        return getArtistName();
    }
}
