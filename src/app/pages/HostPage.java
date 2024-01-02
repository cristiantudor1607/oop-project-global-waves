package app.pages;

import app.pages.features.Announcement;
import app.users.User;
import app.properties.Visitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HostPage extends Page {
    private final User host;
    private final List<Announcement> announcements;

    public HostPage(final User host) {
        this.host = host;
        announcements = new ArrayList<>();
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
     * Returns the name of the host.
     * @return The name of the host, if the page is a {@code HostPage}, {@code null}
     * otherwise
     */
    @Override
    public String getHostName() {
        return host.getUsername();
    }

    /**
     * Returns the page owner username, but only for public pages. It means that
     * it returns either the artist name, or the host name.
     * @return The artist name, or the host name, {@code null} otherwise
     */
    @Override
    public String getUsername() {
        return getHostName();
    }
}
