package globalwaves.player.entities.paging;

import globalwaves.player.entities.Announcement;
import globalwaves.player.entities.Podcast;
import globalwaves.player.entities.User;
import globalwaves.player.entities.properties.Visitor;

import java.util.ArrayList;
import java.util.List;

public class HostPage extends Page{
    private final User host;
    private final List<Podcast> podcasts;
    private final List<Announcement> announcements;

    public HostPage(User host) {
        this.host = host;
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }


    @Override
    public String accept(Visitor v) {
        return null;
    }

}
