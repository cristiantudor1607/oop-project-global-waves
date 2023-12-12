package globalwaves.player.entities.paging;

import globalwaves.player.entities.Artist;
import globalwaves.player.entities.Event;
import globalwaves.player.entities.Merch;
import globalwaves.player.entities.User;
import globalwaves.player.entities.properties.Visitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArtistPage extends Page {
    private final User artist;
    private final List<Event> events;
    private final List<Merch> merchandising;

    public ArtistPage(final User artist) {
        this.artist = artist;
        events = new ArrayList<>();
        merchandising = new ArrayList<>();
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
