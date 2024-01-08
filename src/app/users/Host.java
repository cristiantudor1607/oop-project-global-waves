package app.users;

import app.notifications.Notification;
import app.pages.features.Announcement;
import app.player.entities.Podcast;
import app.pages.HostPage;
import app.pages.Page;
import app.statistics.StatisticsUtils;
import app.utilities.SortByIntegerValue;
import app.utilities.constants.NotificationConstants;
import app.utilities.constants.StatisticsConstants;
import lombok.Getter;

import java.util.*;

@Getter
public class Host extends User {
    private final List<Podcast> podcasts;
    private final HostPage selfPage;

    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        setSubscription(SubscriptionType.PROVIDER);
        podcasts = new ArrayList<>();
        selfPage = new HostPage(this);
    }

    public Host(final String username) {
        super(username);
        setSubscription(SubscriptionType.PROVIDER);
        podcasts = new ArrayList<>();
        selfPage = new HostPage(this);
    }

    /**
     * Returns the user statistics.
     * @return A map which stores the criteria as the key, and a list of tuples of object name
     * and listen count as value, if there isn't specified otherwise. <br>
     * For users, the criteria are:
     * <ul>
     *     <li>topArtist</li>
     *     <li>topGenres</li>
     *     <li>topSongs</li>
     *     <li>topAlbums</li>
     *     <li>topEpisodes</li>
     * </ul>
     * For artists, the criteria are:
     * <ul>
     *     <li>topAlbum</li>
     *     <li>topSongs</li>
     *     <li>topFans: <b>for this criterion, the list will contains tuples with irrelevant
     *     Integer values</b></li>
     *     <li>listeners: <b>for this criterion, the list will contains only 1 tuple,
     *     with irrelevant String value. It will be set by default to {@code "listeners"}</b></li>
     * </ul>
     * For hosts, the criteria are:
     * <ul>
     *     <li>topEpisodes</li>
     *     <<li>listeners: <b>for this criterion, the list will contains only 1 tuple,
     *     with irrelevant String value. It will be set by default to {@code "listeners"}</b></li>
     * </ul>
     *
     */
    @Override
    public Map<String, List<Map.Entry<String, Integer>>> getStatistics() {
        Map<String, List<Map.Entry<String, Integer>>> statistics  = new HashMap<>();

        List<Map.Entry<String, Integer>> episodes = StatisticsUtils.parseHistory(episodeHistory,
                new SortByIntegerValue<>());
        statistics.put(StatisticsConstants.TOP_EPISODES, episodes);

        int listenersNumber = peopleHistory.size();
        List<Map.Entry<String, Integer>> listenersMapList = new ArrayList<>();
        listenersMapList.add(new AbstractMap.SimpleEntry<>("listeners", listenersNumber));
        statistics.put(StatisticsConstants.LISTENERS, listenersMapList);

        return statistics;
    }

    /**
     * Checks if user has something in history.
     * @return {@code true}, if {@code this} user has history, {@code false} otherwise
     */
    @Override
    public boolean hasHistoryData() {
        // FIXME: If something is wrong, it can be from here
        return !peopleHistory.isEmpty() || !episodeHistory.isEmpty();
    }

    @Override
    public void trackFan(final User user) {
        if (!peopleHistory.containsKey(user)) {
            peopleHistory.put(user, 0);
        }

        int listens = peopleHistory.get(user);
        peopleHistory.put(user, ++listens);
    }

    /**
     * Returns the page of the host.
     * @return The page of the host
     */
    @Override
    public Page getPage() {
        return selfPage;
    }

    /**
     * Adds a podcast to the host podcast list.
     * @param newPodcast The new podcast to be added
     * @return {@code true}, if the user is a host, {@code false} otherwise
     */
    @Override
    public boolean addPodcast(final Podcast newPodcast) {
        return podcasts.add(newPodcast);
    }

    /**
     * Removes the podcast from host list of podcasts.
     * @param oldPodcast The podcast to be removed. It has to exist in the host list
     */
    @Override
    public void removePodcast(final Podcast oldPodcast) {
        podcasts.remove(oldPodcast);
    }

    /**
     * Returns the podcast with the given name.
     * @param podcastName The name of the podcast
     * @return The podcast, if it exists, {@code null}, if the host doesn't have a podcast
     * with the given name, or the user isn't a host
     */
    @Override
    public Podcast getPodcast(final String podcastName) {
        for (Podcast p : podcasts) {
            if (p.getName().equals(podcastName)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Adds a new announcement.
     * @param newAnnouncement The new announcement to be added
     * @return {@code true}, if the user is a host, {@code false} otherwise
     */
    @Override
    public boolean addAnnouncement(final Announcement newAnnouncement) {
        notifier.insertInfo(new Notification(getUsername(),
                NotificationConstants.NEW_ANNOUNCEMENT));
        return selfPage.getAnnouncements().add(newAnnouncement);
    }

    /**
     * Removes the announcement.
     * @param oldAnnouncement The announcement to be removed
     */
    @Override
    public void removeAnnouncement(final Announcement oldAnnouncement) {
        selfPage.getAnnouncements().remove(oldAnnouncement);
    }

    /**
     * Returns the announcement with the given name
     * @param announceName The name of the announcement
     * @return The announcement, if it exists, {@code null}, if the host doesn't have an
     * announcement with the given name, or the user isn't a host
     */
    @Override
    public Announcement getAnnouncement(final String announceName) {
        for (Announcement announce: selfPage.getAnnouncements()) {
            if (announce.name().equals(announceName)) {
                return announce;
            }
        }

        return null;
    }

    /**
     * Checks if the user is a normal user.
     * @return {@code true}, if the user is a normal user, {@code false}, otherwise
     */
    @Override
    public boolean isNormalUser() {
        return false;
    }

    /**
     * Checks if the user is a host.
     * @return {@code true}, if the user is a host, {@code false}, otherwise
     */
    @Override
    public boolean isHost() {
        return true;
    }
}
