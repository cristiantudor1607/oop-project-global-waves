package app.pages;

import app.pages.features.Merch;
import app.properties.Visitor;
import app.properties.Visitable;
import app.users.User;

import java.util.Optional;

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

    /**
     * Returns the artist, if {@code this} is an {@code ArtistPage}
     * @return An {@code Optional} describing the artist, if {@code this} is an artist page,
     * or an empty {@code Optional} otherwise
     */
    public Optional<User> getArtist() {
        return Optional.empty();
    }

    /**
     * Returns the artist, if {@code this} is an artist page, the host if {@code this}
     * is a host page, or nothing otherwise
     * @return An {@code Optional} containing the public person, if {@code this}
     * is an artist page, or a host page, an empty {@code Optional} otherwise
     */
    public Optional<User> getPublicUser() {
        return Optional.empty();
    }

    /**
     * Returns the merch with the given name, for artist pages, if the artist has one.
     * @param name The name of the merch
     * @return An {@code Optional} describing the merch, if {@code this} is an artist page,
     * and the artist has the merch with the given name, or
     * an empty {@code Optional} otherwise
     */
    public Optional<Merch> getMerchByName(final String name) {
        return Optional.empty();
    }
}
