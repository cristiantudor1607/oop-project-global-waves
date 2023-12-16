package app.management;

import app.enums.PageType;
import app.enums.UserType;
import app.exitstats.stageone.*;
import app.exitstats.stagetwo.*;
import app.commands.stageone.*;
import app.commands.stagetwo.*;
import app.pages.features.Announcement;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.parser.commands.templates.CommandObject;
import app.player.entities.*;
import app.users.AdminBot;
import app.pages.Page;
import app.pages.ContentVisitor;
import app.properties.PlayableEntity;
import app.utilities.DateMapper;
import app.users.User;
import app.utilities.HelperTool;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;


@Getter @Setter
public class ActionManager {
    private static ActionManager instance;
    private AdminBot adminBot;
    private HelperTool tool;
    private ContentVisitor contentVisitor;
    private int lastActionTime;

    private Map<String, UserInterface> userInterfaces;

    private ActionManager() {
        adminBot = new AdminBot();
        tool = HelperTool.getInstance();
        contentVisitor = new ContentVisitor();
        lastActionTime = 0;

        userInterfaces = new HashMap<>();
        for (User user: adminBot.getDatabase().getUsers()) {
            userInterfaces.put(user.getUsername(), new UserInterface(user));
        }
    }

    public static ActionManager getInstance() {
        if (instance == null)
            instance = new ActionManager();

        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }


    private Player getPlayerByUsername(final String username) {
        UserInterface ui = userInterfaces.get(username);

        return ui.getPlayer();
    }

    private SearchBar getSearchBarByUsername(final String username) {
        UserInterface ui = userInterfaces.get(username);

        return ui.getSearchbar();
    }

    private User getProfileByUsername(final String username) {
        UserInterface ui = userInterfaces.get(username);

        return ui.getProfile();
    }

    private Page getPageByUsername(final String username) {
        UserInterface ui = userInterfaces.get(username);

        return ui.getCurrentPage();
    }

    /**
     * Checks if at least one of the users have an active connection with the user.
     * An active connection means that someone listen its album, one of its songs, or
     * its playlist.
     * @param username The user to be checked
     * @return true, if the user has active connections with others, false otherwise
     */
    public boolean userIsBeingListened(@NonNull final String username) {
        for (UserInterface ui: userInterfaces.values()) {
            // Ignore the user that we want to delete
            if (ui.getProfile().getUsername().equals(username))
                continue;

            // Ignore the users that don't listen to anything
            PlayableEntity playingCollection = ui.getPlayer().getSelectedSource();
            if (playingCollection == null)
                continue;

            // This method will return the name of the artist, or the host, or the owner
            // of the playlist
            String publicPerson = ui.getPlayer().getSelectedSource().getPublicPerson();
            if (publicPerson.equals(username))
                return true;

            // If there's a playlist on the player, and it has songs from artist,
            // ot if there's a host podcast playing
            if (playingCollection.hasAudiofileFromUser(username))
                return true;
        }

        return false;
    }

    /**
     * Checks if someone is on user page.
     * @param username The user to be verified
     * @return true, if someone is on user's page, or false otherwise
     */
    public boolean userIsBeingWatched(@NonNull final String username) {
        for (UserInterface ui: userInterfaces.values()) {
            String pageOwner = ui.getCurrentPage().getUsername();
            if (pageOwner == null)
                continue;

            if (pageOwner.equals(username))
                return true;
        }

        return false;
    }

    /**
     * Checks if the given album is being listened in at least one of the players
     * @param album The album to be searched
     * @return true, if someone is listening the album or a song from the album
     */
    public boolean albumIsBeingListened(@NonNull final Album album) {
        for (UserInterface ui : userInterfaces.values()) {
            Player userPlayer = ui.getPlayer();
            if (userPlayer.isPlayingFromAlbum(album))
                return true;
        }

        return false;
    }

    /**
     * Checks if the user that gave the command is online
     * @param execQuery The command that is being executed
     * @return true, if the user is online, false, otherwise
     */
    public boolean requestApprovalForAction(CommandObject execQuery) {
        User queriedUser = adminBot.getUserByUsername(execQuery.getUsername());

        return queriedUser.isOnline();
    }


    /**
     * Wrapper over the getPlayerByUsername to be used as a request
     * @param execQuery the CommandObject that wants to access the player
     *                  of the user
     * @return The player, if the user with the given username exists, false
     * otherwise ( if the username doesn't exist, or it is used by an artist or a host)
     */
    public Player requestPlayer(CommandObject execQuery) {
        String username = execQuery.getUsername();

        return getPlayerByUsername(username);
    }

    public List<String> requestSearchResults(SearchInterrogator execQuery) {
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);
        SearchBar searchbar = getSearchBarByUsername(username);

        userPlayer.stopPlayer();
        searchbar.search(execQuery.getType(), execQuery.getFilters());
        if (searchbar.hasSearchedPages())
            return searchbar.getPagesAsNames();

        return searchbar.getResultsAsNames();
    }

    public SelectExit.Status requestItemSelection(SelectInterrogator executingSelect) {
        int itemNumber = executingSelect.getItemNumber();
        String username = executingSelect.getUsername();

        SearchBar searchbar = getSearchBarByUsername(username);
        UserInterface userUI = userInterfaces.get(username);
        Player userPlayer = getPlayerByUsername(username);

        if (searchbar.wasNotInvoked())
            return SelectExit.Status.NO_LIST;

        if (searchbar.invalidItem(itemNumber) || searchbar.hasNoSearchResult())
            return SelectExit.Status.OUT_OF_BOUNDS;

        return switch (searchbar.getTypeOfSearch()) {
            case PLAYABLE_ENTITY -> {
                PlayableEntity entity = searchbar.getResultAtIndex(itemNumber - 1);
                userPlayer.select(entity);
                executingSelect.setSelectedPlayableEntity(entity);

                // Reset the searchbar results after selecting the object
                searchbar.resetResults();
                yield SelectExit.Status.SELECTED_PLAYABLE_ENTITY;
            }
            case PAGE -> {
                Page foundPage = searchbar.getPageAtIndex(itemNumber - 1);
                userUI.setCurrentPage(foundPage);
                executingSelect.setSelectedPage(foundPage);
                yield SelectExit.Status.SELECTED_PAGE;
            }
        };
    }

    public LoadExit.Status requestLoading(LoadInterrogator executingLoad) {
        String username = executingLoad.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (!userPlayer.hasSourceSelected())
            return LoadExit.Status.NO_SOURCE_SELECTED;

        if (userPlayer.hasNoSource())
            return LoadExit.Status.NO_SOURCE_SELECTED;

        if (userPlayer.hasEmptySource())
            return LoadExit.Status.EMPTY_SOURCE;

        userPlayer.load(userPlayer.getSelectedSource());

        return LoadExit.Status.LOADED;
    }

    public PlayPauseExit.Status requestUpdateState(PlayPauseInterrogator executingQuery) {
        String username = executingQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource())
            return PlayPauseExit.Status.NO_SOURCE;

        if (userPlayer.isPlaying()) {
            userPlayer.pause();
            return PlayPauseExit.Status.PAUSED;
        }

        userPlayer.play();
        return PlayPauseExit.Status.RESUMED;
    }

    public CreationExit.Status requestPlaylistCreation(CreatePlaylistInterrogator execQuery) {
        String owner = execQuery.getUsername();
        String playlistName = execQuery.getPlaylistName();
        int timestamp = execQuery.getTimestamp();

        if (adminBot.checkIfOwnerHasPlaylist(owner, playlistName))
            return CreationExit.Status.ALREADY_EXISTS;

        adminBot.createPlaylist(owner, playlistName, timestamp);

        return CreationExit.Status.CREATED;
    }

    public SwitchVisibilityExit.Status requestSwitchVisibility(VisibilityInterrogator execQuery) {
        int id = execQuery.getPlaylistId();
        String owner = execQuery.getUsername();

        Playlist ownerPlaylist = adminBot.getOwnerPlaylistById(owner, id);
        if (ownerPlaylist == null)
            return SwitchVisibilityExit.Status.TOO_HIGH;

        if (ownerPlaylist.isPublic()) {
            ownerPlaylist.makePrivate();
            return SwitchVisibilityExit.Status.MADE_PRIVATE;
        }

        ownerPlaylist.makePublic();

        return SwitchVisibilityExit.Status.MADE_PUBLIC;
    }

    public AddRemoveExit.Status requestAddRemove(AddRemoveInterrogator execQuery) {
        String ownername = execQuery.getUsername();
        int id = execQuery.getPlaylistId();

        Player ownerPlayer = getPlayerByUsername(ownername);
        Playlist ownerPlaylist = adminBot.getOwnerPlaylistById(ownername, id);

        if (ownerPlaylist == null)
            return AddRemoveExit.Status.INVALID_PLAYLIST;

        if (!ownerPlayer.hasSourceLoaded())
            return AddRemoveExit.Status.NO_SOURCE;

        AudioFile playingFile = ownerPlayer.getPlayingFile();
        Song workingOnSong = playingFile.getCurrentSong();

        // If getCurrentSong returned null, then the playing file is not a song
        if (workingOnSong == null)
            return AddRemoveExit.Status.NOT_A_SONG;

        if (!ownerPlaylist.hasSong(workingOnSong)) {
            ownerPlaylist.addSong(workingOnSong);
            workingOnSong.increasePlaylistsCount();
            return AddRemoveExit.Status.ADDED;
        }

        ownerPlaylist.removeSong(workingOnSong);
        workingOnSong.decreasePlaylistsCount();
        return AddRemoveExit.Status.REMOVED;
    }

    public List<Playlist> requestOwnerPlaylists(final String owner) {
        return adminBot.getOwnerPlaylists(owner);
    }

    public LikeExit.Status requestLikeAction(LikeInterrogator execQuery) {
        String username = execQuery.getUsername();

        User user = getProfileByUsername(username);
        Player userPlayer = getPlayerByUsername(username);

        if (!userPlayer.hasSourceLoaded())
            return LikeExit.Status.NO_SOURCE;

        AudioFile playingFile = userPlayer.getPlayingFile();
        Song workingOnSong = playingFile.getCurrentSong();

        // If getWorkingSong method returned null, it means that the selected source
        // is not a song
        if (workingOnSong == null)
            return LikeExit.Status.NOT_A_SONG;

        if (!user.isLikingSong(workingOnSong)) {
            user.like(workingOnSong);
            workingOnSong.addLike();
            return LikeExit.Status.LIKED;
        }

        user.unlike(workingOnSong);
        workingOnSong.removeLike();
        return LikeExit.Status.UNLIKED;
    }

    public List<String> requestLikedSongs(ShowLikesInterrogator execQuery) {
        String username = execQuery.getUsername();

        List<Song> songs = adminBot.getUserLikedSongs(username);

        List<String> names = new ArrayList<>();
        for (Song s : songs)
            names.add(s.getName());

        return names;
    }

    public FollowExit.Status requestFollowAction(FollowInterrogator execQuery) {
        String username = execQuery.getUsername();

        User user = getProfileByUsername(username);
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource())
            return FollowExit.Status.NO_SOURCE;

        PlayableEntity playing = userPlayer.getSelectedSource();
        Playlist workingOnPlaylist = playing.getCurrentPlaylist();

        // If getCurrentPlaylist method returned null, it means that the source
        // is not a playlist
        if (workingOnPlaylist == null)
            return FollowExit.Status.NOT_A_PLAYLIST;

        if (workingOnPlaylist.isOwnedByUser(user))
            return FollowExit.Status.OWNER;

        if (!workingOnPlaylist.isFollowedByUser(user)) {
            workingOnPlaylist.getFollowedBy(user);
            user.follow(workingOnPlaylist);
            return FollowExit.Status.FOLLOWED;
        }

        workingOnPlaylist.getUnfollowedBy(user);
        user.unfollow(workingOnPlaylist);
        return FollowExit.Status.UNFOLLOWED;

    }

    public String requestRepeatAction(RepeatInterrogator execQuery) {
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);

        if (!userPlayer.hasSourceLoaded() || userPlayer.hasNoSource())
            return "Please load a source before setting the repeat status.";

        userPlayer.changeRepeatState();
        int newRepeat = userPlayer.getRepeat();
        String stateName = userPlayer.getSelectedSource().getRepeatStateName(newRepeat);
        return "Repeat mode changed to " + stateName.toLowerCase() + ".";
    }

    public ShuffleExit.Status requestShuffling(ShuffleInterrogator execQuery) {
        int seed = execQuery.getSeed();
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);

        if (!userPlayer.hasSourceLoaded() || userPlayer.hasNoSource())
            return ShuffleExit.Status.NO_SOURCE_LOADED;

        ShuffleExit.Status shuffleExit;
        if (!userPlayer.isShuffle()) {
            if (userPlayer.shuffle(seed))
                shuffleExit = ShuffleExit.Status.ACTIVATED;
            else
                shuffleExit = ShuffleExit.Status.NOT_A_PLAYLIST;
        } else {
            if (userPlayer.unshuffle())
                shuffleExit = ShuffleExit.Status.DEACTIVATED;
            else
                shuffleExit = ShuffleExit.Status.NOT_A_PLAYLIST;
        }

        return shuffleExit;
    }

    public String requestNext(NextInterrogator execQuery) {
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);

       if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
           return "Please load a source before skipping to the next track.";

       if (userPlayer.playNext()) {
           String track = userPlayer.getPlayingFile().getName();
           return "Skipped to next track successfully. The current track is " + track + ".";
       }

        return "Please load a source before skipping to the next track.";
    }

    public String requestPrev(PrevInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before returning to the previous track.";

        userPlayer.playPrev(execQuery.getTimestamp() - lastActionTime);
        return "Returned to previous track successfully. The current track is " +
                userPlayer.getPlayingFile().getName() + ".";
    }

    public String requestForward(ForwardInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before attempting to forward.";

        if (userPlayer.getSelectedSource().cantGoForwardOrBackward())
            return "The loaded source is not a podcast.";

        userPlayer.skip();
        return "Skipped forward successfully.";
    }

    public String requestBackward(BackwardInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before rewinding.";

        if (userPlayer.getSelectedSource().cantGoForwardOrBackward())
            return "The loaded source is not a podcast.";

        userPlayer.rewound();
        return "Rewound successfully.";
    }

    public List<String> requestTopFiveSongs() {
        return adminBot.getTopFiveSongs();
    }

    public List<String> requestTopFivePlaylists() {
        return adminBot.getTopFivePlaylists();
    }

    public SwitchConnectionExit.Status requestSwitchConnection(ConnectionInterrogator execQuery) {
        String username = execQuery.getUsername();

        User queriedUser = adminBot.getUserByUsername(username);

        if (queriedUser == null)
            return SwitchConnectionExit.Status.INVALID_USERNAME;

        if (!queriedUser.isNormalUser())
            return SwitchConnectionExit.Status.NOT_NORMAL;

        Player userPlayer = getPlayerByUsername(username);
        if (userPlayer.isFreeze())
            userPlayer.unfreeze();
        else
            userPlayer.freeze();
        queriedUser.switchStatus();
        return SwitchConnectionExit.Status.SUCCESS;
    }

    public AddUserExit.Status requestAddingUser(AddUserInterrogator execQuery) {
        boolean usernameExists = adminBot.checkUsername(execQuery.getUsername());
        if (usernameExists)
            return AddUserExit.Status.USERNAME_TAKEN;

        // Transform the string into a type
        UserType.Type newUserType = UserType.parseString(execQuery.getType());
        if (newUserType == UserType.Type.UNKNOWN)
            return AddUserExit.Status.ERROR;

        // Extract the infos of the new user
        String username = execQuery.getUsername();
        int age = execQuery.getAge();
        String city = execQuery.getCity();

        User newUser = adminBot.createUser(username, age, city, newUserType);
        adminBot.addUser(newUser);
        // Create an interface for the User
        if (newUser.isNormalUser())
            userInterfaces.put(username, new UserInterface(newUser));

        return AddUserExit.Status.SUCCESS;
    }

    public AddAlbumExit.Status requestAddingAlbum(AddAlbumInterrogator execQuery) {
        boolean usernameExist = adminBot.checkUsername(execQuery.getUsername());
        if (!usernameExist)
            return AddAlbumExit.Status.INVALID_USERNAME;

        User artist = adminBot.getArtistByUsername(execQuery.getUsername());
        if (artist == null)
            return AddAlbumExit.Status.NOT_ARTIST;

        String albumName = execQuery.getAlbumName();
        if (adminBot.checkAlbumNameForUser(artist, albumName))
            return AddAlbumExit.Status.SAME_NAME;

        if (tool.hasSameSongAtLeastTwice(execQuery.getSongs()))
            return AddAlbumExit.Status.SAME_SONG;

        String artistName = execQuery.getUsername();
        String description = execQuery.getDescription();
        int creationTime = execQuery.getTimestamp();

        // Set the creation time for all songs on the album
        tool.setCreationTimestamp(execQuery.getSongs(), creationTime);
        // Add new songs to library
        adminBot.addSongsToLibrary(artistName, execQuery.getSongs());
        // Create the album object
        Album artistNewAlbum = new Album(artistName, albumName, description,
                creationTime, execQuery.getSongs());
        // Add album to user albums
        artist.addAlbum(artistNewAlbum);

        return AddAlbumExit.Status.SUCCESS;
    }

    public AddPodcastExit.Status requestAddingPodcast(final AddPodcastInterrogator execQuery) {
        String hostName = execQuery.getUsername();

        if (!adminBot.checkUsername(hostName))
            return AddPodcastExit.Status.DOESNT_EXIST;

        User host = adminBot.getHostByUsername(hostName);
        if (host == null)
            return AddPodcastExit.Status.NOT_HOST;

        String podcastName = execQuery.getPodcastName();
        if (adminBot.checkPodcastNameForUser(host, podcastName))
            return AddPodcastExit.Status.SAME_NAME;

        if (tool.hasSameElementTwice(execQuery.getEpisodes()))
            return AddPodcastExit.Status.DUPLICATE;

        // Create the new Podcast Object
        Podcast hostPodcast = new Podcast(podcastName, hostName, execQuery.getEpisodes());
        // Add Podcast to Library
        adminBot.addPodcastToLibrary(hostName, hostPodcast);
        // Add Podcast to user podcasts
        host.addPodcast(hostPodcast);

        return AddPodcastExit.Status.SUCCESS;
    }

    public List<Album> requestUserAlbums(final ShowAlbumsInterrogator execQuery) {
        String username = execQuery.getUsername();

        return adminBot.getArtistAlbums(username);
    }

    public List<Podcast> requestUserPodcasts(final ShowPodcastsInterrogator execQuery) {
        String hostName = execQuery.getUsername();

        return adminBot.getHostPodcasts(hostName);
    }

    public String requestPageContent(final PrintPageInterrogator execQuery) {
        String username = execQuery.getUsername();

        Page userPage = getPageByUsername(username);

        return userPage.accept(contentVisitor);
    }

    public AddEventExit.Status requestAddingEvent(final AddEventInterrogator execQuery) {
        String username = execQuery.getUsername();

        if (!adminBot.checkUsername(username))
            return AddEventExit.Status.DOESNT_EXIST;

        User artist = adminBot.getArtistByUsername(username);
        if (artist == null)
            return AddEventExit.Status.NOT_ARTIST;

        String eventName = execQuery.getName();
        String eventDescription = execQuery.getDescription();
        LocalDate eventDate = DateMapper.parseStringToDate(execQuery.getDate());

        if (artist.hasEvent(eventName))
            return AddEventExit.Status.SAME_NAME;

        if (eventDate == null)
            return AddEventExit.Status.INVALID_DATE;

        artist.addEvent(new Event(eventName, eventDescription, eventDate));

        return AddEventExit.Status.SUCCESS;
    }

    public AddMerchExit.Status requestAddingMerch(final AddMerchInterrogator execQuery) {
        String username = execQuery.getUsername();

        if (!adminBot.checkUsername(username))
            return AddMerchExit.Status.DOESNT_EXIST;

        User artist = adminBot.getArtistByUsername(username);
        if (artist == null)
            return AddMerchExit.Status.NOT_ARTIST;

        String merchName = execQuery.getName();
        String merchDescription = execQuery.getDescription();
        int merchPrice = execQuery.getPrice();

        if (artist.hasMerch(merchName))
            return AddMerchExit.Status.SAME_NAME;

        if (merchPrice < 0)
            return AddMerchExit.Status.NEGATIVE_PRICE;

        artist.addMerch(new Merch(merchName, merchDescription, merchPrice));

        return AddMerchExit.Status.SUCCESS;
    }

    public RemoveEventExit.Status requestRemovingEvent(final RemoveEventInterrogator execQuery) {
        String username = execQuery.getUsername();

        if (!adminBot.checkUsername(username))
            return RemoveEventExit.Status.DOESNT_EXIST;

        User artist = adminBot.getArtistByUsername(username);
        if (artist == null)
            return RemoveEventExit.Status.NOT_ARTIST;

        String eventName = execQuery.getName();
        Event artistEvent = artist.getEvent(eventName);
        if (artistEvent == null)
            return RemoveEventExit.Status.INVALID_NAME;

        artist.removeEvent(artistEvent);

        return RemoveEventExit.Status.SUCCESS;
    }

    public AddAnnouncementExit.Status
    requestAddingAnnouncement(final AddAnnouncementInterrogator execQuery) {
        String username = execQuery.getUsername();

        if (!adminBot.checkUsername(username))
            return AddAnnouncementExit.Status.DOESNT_EXIST;

        User host = adminBot.getHostByUsername(username);
        if (host == null)
            return AddAnnouncementExit.Status.NOT_HOST;

        String announcementName = execQuery.getName();
        String announcementDescription = execQuery.getDescription();
        if (host.hasAnnouncement(announcementName))
            return AddAnnouncementExit.Status.SAME_NAME;

        host.addAnnouncement(new Announcement(announcementName, announcementDescription));

        return AddAnnouncementExit.Status.SUCCESS;
    }

    public RemoveAnnouncementExit.Status
    requestRemovingAnnouncement(final RemoveAnnouncementInterrogator execQuery) {
        String username =  execQuery.getUsername();

        if (!adminBot.checkUsername(username))
            return RemoveAnnouncementExit.Status.DOESNT_EXIST;

        User host = adminBot.getHostByUsername(username);
        if (host == null)
            return RemoveAnnouncementExit.Status.NOT_HOST;

        String announcementName = execQuery.getName();
        Announcement announcement = host.getAnnouncement(announcementName);
        if (announcement == null)
            return RemoveAnnouncementExit.Status.INVALID_NAME;

        host.removeAnnouncement(announcement);

        return RemoveAnnouncementExit.Status.SUCCESS;
    }


    public List<String> requestOnlineUsers() {
        List<User> onlineUsers = adminBot.getOnlineUsers();

        return tool.getUsernames(onlineUsers);
    }

    public List<String> requestAllUsers() {
        List<User> allUsers = adminBot.getAllUsers();

        return tool.getUsernames(allUsers);
    }

    public String requestDeletingUser(final DeleteUserInterrogator execQuery) {
        String username = execQuery.getUsername();
        User user = adminBot.getUserByUsername(username);

        if (username.equals("melodicwanderer"))
            System.out.println();

        // If getUserByUsername method returned null, it means that the user doesn't exist
        if (user == null)
            return "The username " + username + " doesn't exist.";

        if (userIsBeingListened(username))
            return username + " can't be deleted.";

        if (userIsBeingWatched(username))
            return username + " can't be deleted.";

        if (adminBot.playlistsHaveSongFromArtist(username))
            return username + " can't be deleted.";

        if (user.isNormalUser())
            userInterfaces.remove(username);

        adminBot.removeUser(user);

        return username + " was successfully deleted.";

    }

    public RemoveAlbumExit.Status requestRemovingAlbum(final RemoveAlbumInterrogator execQuery) {
        String username = execQuery.getUsername();
        String albumName = execQuery.getName();

        if (!adminBot.checkUsername(username))
            return RemoveAlbumExit.Status.DOESNT_EXIST;

        User artist = adminBot.getArtistByUsername(username);
        if (artist == null)
            return RemoveAlbumExit.Status.NOT_ARTIST;

        if (!artist.hasAlbumWithName(albumName))
            return RemoveAlbumExit.Status.DONT_HAVE;

        // Retrieve the album from artist. It won't return null,
        // because we already checked if the user is an artist, or
        // if the artist doesn't have an album with that name
        Album album = artist.getAlbumByName(albumName);

        // If there is at least one song from the album used in a playlist
        if (album.isUsedInPlaylist())
            return RemoveAlbumExit.Status.FAIL;

        // If there is at least one player listening to something from album
        if (albumIsBeingListened(album))
            return RemoveAlbumExit.Status.FAIL;

        // Remove the album as an admin
        adminBot.removeAlbum(album);
        return RemoveAlbumExit.Status.SUCCESS;
    }

    public RemovePodcastExit.Status
    requestRemovingPodcast(final RemovePodcastInterrogator execQuery) {
        String username = execQuery.getUsername();
        String podcastName = execQuery.getName();

        if (!adminBot.checkUsername(username))
            return RemovePodcastExit.Status.DOESNT_EXIST;

        User host = adminBot.getHostByUsername(username);
        if (host == null)
            return RemovePodcastExit.Status.NOT_HOST;

        if (!host.hasPodcastWithName(podcastName))
            return RemovePodcastExit.Status.DONT_HAVE;

        if (userIsBeingListened(username))
            return RemovePodcastExit.Status.FAIL;

        Podcast podcast = host.getPodcastByName(podcastName);

        // Remove the podcast from the host point of view
        host.removePodcast(podcast);
        // Remove the podcast from the others point of view
        adminBot.removePodcast(podcast);
        return RemovePodcastExit.Status.SUCCESS;
    }

    public String requestChangePage(final ChangePageInterrogator execQuery) {
        String username = execQuery.getUsername();
        String nextPage = execQuery.getNextPage();

        UserInterface ui = userInterfaces.get(username);

        return switch (PageType.parseString(nextPage)) {
            case HOME -> {
                Page homePage = ui.getHomePage();
                ui.setCurrentPage(homePage);
                yield username + " accessed Home successfully.";
            }
            case LIKED -> {
                Page likedPage = ui.getLikedContentPage();
                ui.setCurrentPage(likedPage);
                yield  username + " accessed LikedContent successfully.";
            }
            case UNKNOWN -> username + " is trying to access a non-existent page.";
        };
    }

    public List<String> requestTopFiveAlbums() {
        return adminBot.getTopFiveAlbums();
    }

    public List<String> requestTopFiveArtists() {
        return adminBot.getTopFiveArtists();
    }


    public void updatePlayersData(CommandObject nextToExecuteCommand) {
        int diff = nextToExecuteCommand.getTimestamp() - lastActionTime;
        for (Map.Entry<String, UserInterface> entry: userInterfaces.entrySet()) {
            Player currPlayer = entry.getValue().getPlayer();
            if (currPlayer.isFreeze())
                continue;

            currPlayer.updatePlayer(diff);
        }
    }
}
