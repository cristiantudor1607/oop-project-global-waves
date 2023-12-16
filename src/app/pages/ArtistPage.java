package app.pages;

import app.pages.features.Event;
import app.pages.features.Merch;
import app.player.entities.*;
import app.properties.Visitor;
import app.users.User;
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

    public List<Album> getAlbums() {
        return artist.getAlbums();
    }

    @Override
    public String accept(Visitor v) {
        return v.visit(this);
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
