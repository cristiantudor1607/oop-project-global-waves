package app.users;

import app.pages.features.Announcement;
import app.player.entities.Podcast;
import app.pages.HostPage;
import app.pages.Page;

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
    public Podcast getPodcastByName(String podcastName) {
        for (Podcast p : podcasts) {
            if (p.getName().equals(podcastName))
                return p;
        }

        return null;
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
    public void removePodcast(Podcast oldPodcast) {
        podcasts.remove(oldPodcast);
    }

    @Override
    public boolean hasAnnouncement(String announceName) {
        for (Announcement announce: selfPage.getAnnouncements()) {
            if (announce.getName().equals(announceName))
                return true;
        }

        return false;
    }

    @Override
    public Announcement getAnnouncement(String announceName) {
        for (Announcement announce: selfPage.getAnnouncements()) {
            if (announce.getName().equals(announceName))
                return announce;
        }

        return null;
    }

    @Override
    public boolean addAnnouncement(Announcement newAnnouncement) {
        return selfPage.getAnnouncements().add(newAnnouncement);
    }

    @Override
    public boolean removeAnnouncement(Announcement oldAnnouncement) {
        return selfPage.getAnnouncements().remove(oldAnnouncement);
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
