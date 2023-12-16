package app.pages;

import app.pages.features.Announcement;
import app.users.User;
import app.properties.Visitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HostPage extends Page{
    private final User host;
    private final List<Announcement> announcements;

    public HostPage(User host) {
        this.host = host;
        announcements = new ArrayList<>();
    }

    @Override
    public String accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String getHostName() {
        return host.getUsername();
    }

    @Override
    public String getUsername() {
        return getHostName();
    }
}
