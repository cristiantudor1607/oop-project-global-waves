package globalwaves.player.entities;

import globalwaves.player.entities.paging.HostPage;
import globalwaves.player.entities.paging.Page;

import java.util.ArrayList;
import java.util.List;

public class Host extends User {
    private final List<Podcast> podcasts;
    private final HostPage selfPage;

    public Host(String username, int age, String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        selfPage = new HostPage(this);
    }

    @Override
    public Page getPage() {
        return selfPage;
    }

    @Override
    public boolean hasPodcastWithName(String podcastName) {
        for (Podcast p: podcasts) {
            if (p.getName().equals(podcastName))
                return true;
        }

        return false;
    }

    @Override
    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    @Override
    public boolean addPodcast(Podcast newPodcast) {
        return podcasts.add(newPodcast);
    }

    @Override
    public boolean isNormalUser() {
        return false;
    }

    @Override
    public boolean isHost() {
        return true;
    }
}
