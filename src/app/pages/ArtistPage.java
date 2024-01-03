package app.pages;

import app.pages.features.Event;
import app.pages.features.Merch;
import app.player.entities.Album;
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

    /**
     * Returns the artist albums.
     * @return A list of albums
     */
    public List<Album> getAlbums() {
        return artist.getAlbums();
    }

    /**
     * Accept method for visitors that returns a string.
     * @param v The visitor
     * @return A string. It depends on the visitor what string contains.
     */
    @Override
    public String accept(final Visitor v) {
        return v.visit(this);
    }

    /**
     * Returns the name of the artist.
     * @return The name of the artist, if the page is an {@code ArtistPage}, {@code null}
     * otherwise
     */
    @Override
    public String getArtistName() {
        return artist.getUsername();
    }

    /**
     * Returns the page owner username, but only for public pages. It means that
     * it returns either the artist name, or the host name.
     * @return The artist name, or the host name, {@code null} otherwise
     */
    @Override
    public String getUsername() {
        return getArtistName();
    }
}
