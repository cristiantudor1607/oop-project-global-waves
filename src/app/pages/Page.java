package app.pages;

import app.properties.Visitor;
import app.properties.Visitable;

public abstract class Page implements Visitable {
    /**
     * Accept method for visitors that returns a string.
     * @param v The visitor
     * @return A string. It depends on the visitor what string contains.
     */
    @Override
    public abstract String accept(Visitor v);

    /**
     * Returns the name of the artist.
     * @return The name of the artist, if the page is an {@code ArtistPage}, {@code null}
     * otherwise
     */
    public String getArtistName() {
        return null;
    }

    /**
     * Returns the name of the host.
     * @return The name of the host, if the page is a {@code HostPage}, {@code null}
     * otherwise
     */
    public String getHostName() {
        return null;
    }

    /**
     * Returns the page owner username, but only for public pages. It means that
     * it returns either the artist name, or the host name.
     * @return The artist name, or the host name, {@code null} otherwise
     */
    public String getUsername() {
        return null;
    }
}
