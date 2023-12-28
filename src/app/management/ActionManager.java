package app.management;

import app.commands.stageone.AddRemoveInterrogator;
import app.commands.stageone.BackwardInterrogator;
import app.commands.stageone.CreatePlaylistInterrogator;
import app.commands.stageone.FollowInterrogator;
import app.commands.stageone.ForwardInterrogator;
import app.commands.stageone.LikeInterrogator;
import app.commands.stageone.LoadInterrogator;
import app.commands.stageone.NextInterrogator;
import app.commands.stageone.PlayPauseInterrogator;
import app.commands.stageone.PrevInterrogator;
import app.commands.stageone.RepeatInterrogator;
import app.commands.stageone.SearchInterrogator;
import app.commands.stageone.SelectInterrogator;
import app.commands.stageone.ShuffleInterrogator;
import app.commands.stageone.VisibilityInterrogator;
import app.commands.stagetwo.AddAlbumInterrogator;
import app.commands.stagetwo.AddAnnouncementInterrogator;
import app.commands.stagetwo.AddEventInterrogator;
import app.commands.stagetwo.AddMerchInterrogator;
import app.commands.stagetwo.AddPodcastInterrogator;
import app.commands.stagetwo.AddUserInterrogator;
import app.commands.stagetwo.ChangePageInterrogator;
import app.commands.stagetwo.ConnectionInterrogator;
import app.commands.stagetwo.DeleteUserInterrogator;
import app.commands.stagetwo.PrintPageInterrogator;
import app.commands.stagetwo.RemoveAlbumInterrogator;
import app.commands.stagetwo.RemoveAnnouncementInterrogator;
import app.commands.stagetwo.RemoveEventInterrogator;
import app.commands.stagetwo.RemovePodcastInterrogator;
import app.commands.stagetwo.ShowAlbumsInterrogator;
import app.commands.stagetwo.ShowPodcastsInterrogator;
import app.enums.PageType;
import app.enums.UserType;
import app.exitstats.stageone.AddRemoveExit;
import app.exitstats.stageone.CreationExit;
import app.exitstats.stageone.FollowExit;
import app.exitstats.stageone.LikeExit;
import app.exitstats.stageone.LoadExit;
import app.exitstats.stageone.PlayPauseExit;
import app.exitstats.stageone.SelectExit;
import app.exitstats.stageone.ShuffleExit;
import app.exitstats.stageone.SwitchVisibilityExit;
import app.exitstats.stagetwo.AddAlbumExit;
import app.exitstats.stagetwo.AddAnnouncementExit;
import app.exitstats.stagetwo.AddEventExit;
import app.exitstats.stagetwo.AddMerchExit;
import app.exitstats.stagetwo.AddPodcastExit;
import app.exitstats.stagetwo.AddUserExit;
import app.exitstats.stagetwo.RemoveAlbumExit;
import app.exitstats.stagetwo.RemoveAnnouncementExit;
import app.exitstats.stagetwo.RemoveEventExit;
import app.exitstats.stagetwo.RemovePodcastExit;
import app.exitstats.stagetwo.SwitchConnectionExit;
import app.pages.features.Announcement;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Album;
import app.player.entities.AudioFile;
import app.player.entities.Player;
import app.player.entities.Playlist;
import app.player.entities.Podcast;
import app.player.entities.SearchBar;
import app.player.entities.Song;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter @Setter
public final class ActionManager {
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

    /**
     * Returns the instance of the class. If instance is null, it creates a new one.
     * @return The instance of the class (it can't return null)
     */
    public static ActionManager getInstance() {
        if (instance == null) {
            instance = new ActionManager();
        }

        return instance;
    }

    /**
     * Resets the instance. It breaks the singleton pattern, but it's designed only for its
     * specific usage inside the action method from main, to separate data for each test. For
     * example, if users were added in a previous test, they won't be visible within other
     * test. If there wasn't the constraint to work only inside the action method, deleteInstance
     * method won't be necessary.
     */
    public static void deleteInstance() {
        instance = null;
    }

    /**
     * Returns the player of the user with the given username.
     * @param username The username of the user whose player is requested
     * @return The player, if the user exists and is tracked by the manager, null, otherwise
     */
    public Player getPlayerByUsername(final String username) {
        UserInterface ui = userInterfaces.get(username);

        return ui == null ? null : ui.getPlayer();
    }

    /**
     * Returns the searchbar of the user with the given username.
     * @param username The username of the user whose searchbar is requested
     * @return The searchbar, if the user exists and is tracked by the manager, null
     * otherwise
     */
    private SearchBar getSearchBarByUsername(final String username) {
        UserInterface ui = userInterfaces.get(username);

        return ui == null ? null : ui.getSearchbar();
    }

    /**
     * Returns the user with the given username, if it exists in the manager tracker.
     * @param username The username of the user
     * @return The user, if it exists, null, otherwise
     */
    private User getProfileByUsername(final String username) {
        UserInterface ui = userInterfaces.get(username);

        return ui == null ? null : ui.getProfile();
    }

    /**
     * Returns the current page of the user with the given username.
     * @param username The username of the user whose page is requested
     * @return The page, if the user exists, and it is tracked by the manager, null,
     * otherwise.
     */
    private Page getPageByUsername(final String username) {
        UserInterface ui = userInterfaces.get(username);

        return ui == null ? null : ui.getCurrentPage();
    }

    /**
     * Checks if the user has an active connection with others. Two users have an active
     * connection if :
     * <ul>
     *     <li>
     *         for normal users: someone is listening to one of their playlists
     *     </li>
     *     <li>
     *         for artists: someone is listening to one of their albums, or at least
     *         one of their songs
     *     </li>
     *     <li>
     *         for hosts: someone is listening to one of their podcasts
     *     </li>
     * </ul>
     * @param username The username of the user to be checked
     * @return true, if the user with the given username is being listened, false
     * otherwise
     */
    public boolean userIsBeingListened(@NonNull final String username) {
        for (UserInterface ui: userInterfaces.values()) {
            // Ignore the user that we want to delete
            if (ui.getProfile().getUsername().equals(username)) {
                continue;
            }

            // Ignore the users that don't listen to anything at all
            PlayableEntity playingCollection = ui.getPlayer().getSelectedSource();
            if (playingCollection == null) {
                continue;
            }

            // Get the name of the user that is being listened in the current player
            String publicPerson = ui.getPlayer().getSelectedSource().getPublicIdentity();
            if (publicPerson.equals(username)) {
                return true;
            }

            // If there's a playlist on the player, and it has songs from artist,
            // or if there's a host podcast playing
            if (playingCollection.hasAudiofileFromUser(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if someone is watching the user page.
     * @param username The username of the user to be checked
     * @return true, if someone is watching user's page, false otherwise
     */
    public boolean userIsBeingWatched(@NonNull final String username) {
        for (UserInterface ui: userInterfaces.values()) {
            // The getUsername method from Page class returns a NonNull String only for
            // artists and hosts
            String pageOwner = ui.getCurrentPage().getUsername();
            if (pageOwner == null) {
                continue;
            }

            if (pageOwner.equals(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the given album is being listened in at least one of the players.
     * @param album The album to be checked
     * @return true, if someone is listening the album or a song from the album, false
     * otherwise
     */
    public boolean albumIsBeingListened(@NonNull final Album album) {
        for (UserInterface ui : userInterfaces.values()) {
            Player userPlayer = ui.getPlayer();
            if (userPlayer.isPlayingFromAlbum(album)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the user that gave the command is online.
     * @param execQuery The command that is being executed
     * @return true, if the user is online, false, otherwise
     */
    public boolean requestApprovalForAction(final CommandObject execQuery) {
        User queriedUser = adminBot.getUserByUsername(execQuery.getUsername());

        return queriedUser.isOnline();
    }

    /**
     * Performs the searching and returns the results as a list of strings.
     * @param execQuery the Search Command that requested the action
     * @return A list with the names of the found entities. If there are no found
     * entities the list is empty
     */
    public List<String> requestSearchResults(final SearchInterrogator execQuery) {
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);
        SearchBar searchbar = getSearchBarByUsername(username);

        userPlayer.stopPlayer();
        searchbar.search(execQuery.getType(), execQuery.getFilters());
        if (searchbar.hasSearchedPages()) {
            return searchbar.getPagesAsNames();
        }

        return searchbar.getResultsAsNames();
    }

    /**
     * Performs the select and returns an exit code.
     * @param execQuery the Select Command that requested the action
     * @return A SelectExit.Status enum which serves as an exit code. It says what type
     * of error was occurred during the select action
     */
    public SelectExit.Status requestItemSelection(final SelectInterrogator execQuery) {
        int itemNumber = execQuery.getItemNumber();
        String username = execQuery.getUsername();

        SearchBar searchbar = getSearchBarByUsername(username);
        UserInterface userUI = userInterfaces.get(username);
        Player userPlayer = getPlayerByUsername(username);

        // Check if there was a search before the select
        if (searchbar.wasNotInvoked()) {
            return SelectExit.Status.NO_LIST;
        }

        // Checks for index out of bounds types of error
        if (searchbar.invalidItem(itemNumber) || searchbar.hasNoSearchResult()) {
            return SelectExit.Status.OUT_OF_BOUNDS;
        }

        return switch (searchbar.getTypeOfSearch()) {
            case PLAYABLE_ENTITY -> {
                PlayableEntity entity = searchbar.getResultAtIndex(itemNumber - 1);
                userPlayer.select(entity);
                execQuery.setSelectedPlayableEntity(entity);

                // Reset the searchbar results after selecting the entity
                searchbar.resetResults();
                yield SelectExit.Status.SELECTED_PLAYABLE_ENTITY;
            }
            case PAGE -> {
                Page foundPage = searchbar.getPageAtIndex(itemNumber - 1);
                userUI.setCurrentPage(foundPage);
                execQuery.setSelectedPage(foundPage);
                yield SelectExit.Status.SELECTED_PAGE;
            }
        };
    }

    /**
     * Loads the previously selected source in the player of the user that gave the
     * command.
     * @param execQuery the Load Command that requested the action
     * @return A LoadExit.Status enum, which serves as an exit code. It says what type
     * of loading error was occurred during the load action
     */
    public LoadExit.Status requestLoading(final LoadInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (!userPlayer.hasSourceSelected() || userPlayer.hasNoSource()) {
            return LoadExit.Status.NO_SOURCE_SELECTED;
        }

        if (userPlayer.hasEmptySource()) {
            return LoadExit.Status.EMPTY_SOURCE;
        }

        userPlayer.load(userPlayer.getSelectedSource());

        return LoadExit.Status.LOADED;
    }

    /**
     * Changes the player state of the user that gave the command.
     * <ul>
     *     <li>
     *         If the player is playing an entity, it is paused
     *     </li>
     *     <li>
     *         If the player is paused, it gets playing state again
     *     </li>
     * </ul>
     * @param execQuery the PlayPause Command that requested the action
     * @return An exit code which describes if an error has occurred, or what type
     * of action was performed (it got played, or it got paused)
     */
    public PlayPauseExit.Status requestPlayPause(final PlayPauseInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource()) {
            return PlayPauseExit.Status.NO_SOURCE;
        }

        if (userPlayer.isPlaying()) {
            userPlayer.pause();
            return PlayPauseExit.Status.PAUSED;
        }

        userPlayer.play();
        return PlayPauseExit.Status.RESUMED;
    }

    /**
     * Creates a playlists for the user that gave the command, if possible.
     * @param execQuery the createPlaylist Command that requested the action
     * @return An exit code which describes if an error was occurred, or it has been
     * created successfully
     */
    public CreationExit.Status
    requestPlaylistCreation(final CreatePlaylistInterrogator execQuery) {
        String owner = execQuery.getUsername();
        String playlistName = execQuery.getPlaylistName();
        int timestamp = execQuery.getTimestamp();

        if (adminBot.checkIfOwnerHasPlaylist(owner, playlistName)) {
            return CreationExit.Status.ALREADY_EXISTS;
        }

        adminBot.createPlaylist(owner, playlistName, timestamp);

        return CreationExit.Status.CREATED;
    }

    /**
     * Changes the visibility of the playlist.
     * @param execQuery the switchVisibility Command that requested the action. It contains
     *                  useful data, such as username of the owner and the playlist id
     * @return An exit code which describes what visibility the playlist has after performing
     * the action, or if an error was occurred.
     */
    public SwitchVisibilityExit.Status
    requestSwitchVisibility(final VisibilityInterrogator execQuery) {
        int id = execQuery.getPlaylistId();
        String owner = execQuery.getUsername();

        Playlist ownerPlaylist = adminBot.getOwnerPlaylistById(owner, id);
        if (ownerPlaylist == null) {
            return SwitchVisibilityExit.Status.TOO_HIGH;
        }

        if (ownerPlaylist.isPublic()) {
            ownerPlaylist.makePrivate();
            return SwitchVisibilityExit.Status.MADE_PRIVATE;
        }

        ownerPlaylist.makePublic();

        return SwitchVisibilityExit.Status.MADE_PUBLIC;
    }

    /**
     * Adds or removes the playing song to the playlist specified inside the command.
     * @param execQuery the addRemoveInPlaylist Command that requested the action. It contains
     *                  the name of the user that wants to add or remove and the id of the
     *                  playlist
     * @return An exit code which describes either the action performed (add/remove), or
     * the error occurred
     */
    public AddRemoveExit.Status requestAddRemove(final AddRemoveInterrogator execQuery) {
        String ownerName = execQuery.getUsername();
        int id = execQuery.getPlaylistId();

        Player ownerPlayer = getPlayerByUsername(ownerName);
        Playlist ownerPlaylist = adminBot.getOwnerPlaylistById(ownerName, id);

        if (ownerPlaylist == null) {
            return AddRemoveExit.Status.INVALID_PLAYLIST;
        }

        if (!ownerPlayer.hasSourceLoaded()) {
            return AddRemoveExit.Status.NO_SOURCE;
        }

        AudioFile playingFile = ownerPlayer.getPlayingFile();
        Song workingOnSong = playingFile.getCurrentSong();

        // If getCurrentSong returned null, then the playing file is not a song
        if (workingOnSong == null) {
            return AddRemoveExit.Status.NOT_A_SONG;
        }

        if (!ownerPlaylist.hasSong(workingOnSong)) {
            ownerPlaylist.addSong(workingOnSong);
            workingOnSong.increasePlaylistsCount();
            return AddRemoveExit.Status.ADDED;
        }

        ownerPlaylist.removeSong(workingOnSong);
        workingOnSong.decreasePlaylistsCount();
        return AddRemoveExit.Status.REMOVED;
    }

    /**
     * Retrieves the user's playlists from database and returns their names as a List.
     * @param owner The name of the user whose playlists are requested
     * @return A list of strings, containing the names of the playlists
     */
    public List<Playlist> requestOwnerPlaylists(final String owner) {
        return adminBot.getOwnerPlaylists(owner);
    }

    /**
     * Likes or unlikes the playing song, if possible.
     * @param execQuery The like Command that requested the action. It contains the name
     *                  of the user
     * @return An exit code, which describes either the type of action performed (like/unlike), or
     * the error occurred
     */
    public LikeExit.Status requestLikeAction(final LikeInterrogator execQuery) {
        String username = execQuery.getUsername();

        User user = getProfileByUsername(username);
        Player userPlayer = getPlayerByUsername(username);

        if (!userPlayer.hasSourceLoaded()) {
            return LikeExit.Status.NO_SOURCE;
        }

        AudioFile playingFile = userPlayer.getPlayingFile();
        Song workingOnSong = playingFile.getCurrentSong();

        // If getWorkingSong method returned null, it means that the selected source
        // is not a song
        if (workingOnSong == null) {
            return LikeExit.Status.NOT_A_SONG;
        }

        if (!user.isLikingSong(workingOnSong)) {
            user.like(workingOnSong);
            workingOnSong.addLike();
            return LikeExit.Status.LIKED;
        }

        user.unlike(workingOnSong);
        workingOnSong.removeLike();
        return LikeExit.Status.UNLIKED;
    }

    /**
     * Retrieves the liked songs from database and returns a list containing their names.
     * @param username The name of the user whose liked songs are requested
     * @return A list of strings, containing the names of the songs
     */
    public List<String> requestLikedSongs(final String username) {

        List<Song> songs = adminBot.getUserLikedSongs(username);

        List<String> names = new ArrayList<>();
        for (Song s : songs) {
            names.add(s.getName());
        }

        return names;
    }

    /**
     * Follows or unfollows the playing playlist, if possible.
     * @param execQuery The follow Command that requested the action. It contains the name
     *                  of the user that gave the command
     * @return An exit code, which describes either the type of action performed (follow/unfollow),
     * or the error occurred.
     */
    public FollowExit.Status requestFollowAction(final FollowInterrogator execQuery) {
        String username = execQuery.getUsername();

        User user = getProfileByUsername(username);
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource()) {
            return FollowExit.Status.NO_SOURCE;
        }

        PlayableEntity playing = userPlayer.getSelectedSource();
        Playlist workingOnPlaylist = playing.getCurrentPlaylist();

        // If getCurrentPlaylist method returned null, it means that the source
        // is not a playlist
        if (workingOnPlaylist == null) {
            return FollowExit.Status.NOT_A_PLAYLIST;
        }

        if (workingOnPlaylist.isOwnedByUser(user)) {
            return FollowExit.Status.OWNER;
        }

        if (!workingOnPlaylist.isFollowedByUser(user)) {
            workingOnPlaylist.getFollowedBy(user);
            user.follow(workingOnPlaylist);
            return FollowExit.Status.FOLLOWED;
        }

        workingOnPlaylist.getUnfollowedBy(user);
        user.unfollow(workingOnPlaylist);
        return FollowExit.Status.UNFOLLOWED;

    }

    /**
     * Changes the repeat state of the player, if possible.
     * @param execQuery The repeat Command that requested the action. It contains the name
     *                  of the user that sent the command.
     * @return An exit message
     */
    public String requestRepeatAction(final RepeatInterrogator execQuery) {
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);

        if (!userPlayer.hasSourceLoaded() || userPlayer.hasNoSource()) {
            return "Please load a source before setting the repeat status.";
        }

        userPlayer.changeRepeatState();
        int newRepeat = userPlayer.getRepeat();
        String stateName = userPlayer.getSelectedSource().getRepeatStateName(newRepeat);
        return "Repeat mode changed to " + stateName.toLowerCase() + ".";
    }

    /**
     * Shuffles the playing source, if possible.
     * @param execQuery The shuffle Command that requested the action. It contains the name
     *                  of the user that sent the command.
     * @return An exit code, which describes either the type of action performed (shuffle/
     * unshuffle), or the error occurred
     */
    public ShuffleExit.Status requestShuffling(final ShuffleInterrogator execQuery) {
        int seed = execQuery.getSeed();
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);

        if (!userPlayer.hasSourceLoaded() || userPlayer.hasNoSource()) {
            return ShuffleExit.Status.NO_SOURCE_LOADED;
        }

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

    /**
     * Plays the next song or episode, taking into account the current repeat state.
     * @param execQuery The next Command that requested the action. It contains the name
     *                  of the user whose playing file will be changed
     * @return A specific exit message
     */
    public String requestNext(final NextInterrogator execQuery) {
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);

       if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded()) {
           return "Please load a source before skipping to the next track.";
       }

       if (userPlayer.playNext()) {
           String track = userPlayer.getPlayingFile().getName();
           return "Skipped to next track successfully. The current track is " + track + ".";
       }

        return "Please load a source before skipping to the next track.";
    }

    /**
     * Plays the previous song or episode, taking into account the current repeat state.
     * @param execQuery The prev Command that requested the action. It contains the name of
     *                  the user whose playing file will be changed
     * @return A specific exit message
     */
    public String requestPrev(final PrevInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded()) {
            return "Please load a source before returning to the previous track.";
        }

        userPlayer.playPrev(execQuery.getTimestamp() - lastActionTime);
        return "Returned to previous track successfully. The current track is " +
                userPlayer.getPlayingFile().getName() + ".";
    }

    /**
     * Goes forward by 90 seconds, or skip to the next track, if it can't.
     * @param execQuery The forward Command that requested the action
     * @return An exit message
     */
    public String requestForward(ForwardInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded()) {
            return "Please load a source before attempting to forward.";
        }

        if (userPlayer.getSelectedSource().cantGoForwardOrBackward()) {
            return "The loaded source is not a podcast.";
        }

        userPlayer.skip();
        return "Skipped forward successfully.";
    }

    public String requestBackward(BackwardInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded()) {
            return "Please load a source before rewinding.";
        }

        if (userPlayer.getSelectedSource().cantGoForwardOrBackward()) {
            return "The loaded source is not a podcast.";
        }

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

        if (queriedUser == null) {
            return SwitchConnectionExit.Status.INVALID_USERNAME;
        }

        if (!queriedUser.isNormalUser()) {
            return SwitchConnectionExit.Status.NOT_NORMAL;
        }

        Player userPlayer = getPlayerByUsername(username);
        if (userPlayer.isFreeze()) {
            userPlayer.unfreeze();
        } else
            userPlayer.freeze();
        queriedUser.switchStatus();
        return SwitchConnectionExit.Status.SUCCESS;
    }

    public AddUserExit.Status requestAddingUser(AddUserInterrogator execQuery) {
        boolean usernameExists = adminBot.checkUsername(execQuery.getUsername());
        if (usernameExists) {
            return AddUserExit.Status.USERNAME_TAKEN;
        }

        // Transform the string into a type
        UserType.Type newUserType = UserType.parseString(execQuery.getType());
        if (newUserType == UserType.Type.UNKNOWN) {
            return AddUserExit.Status.ERROR;
        }

        // Extract the infos of the new user
        String username = execQuery.getUsername();
        int age = execQuery.getAge();
        String city = execQuery.getCity();

        User newUser = adminBot.createUser(username, age, city, newUserType);
        adminBot.addUser(newUser);
        // Create an interface for the User
        if (newUser.isNormalUser()) {
            userInterfaces.put(username, new UserInterface(newUser));
        }

        return AddUserExit.Status.SUCCESS;
    }

    public AddAlbumExit.Status requestAddingAlbum(AddAlbumInterrogator execQuery) {
        boolean usernameExist = adminBot.checkUsername(execQuery.getUsername());
        if (!usernameExist) {
            return AddAlbumExit.Status.INVALID_USERNAME;
        }

        User artist = adminBot.getArtistByUsername(execQuery.getUsername());
        if (artist == null) {
            return AddAlbumExit.Status.NOT_ARTIST;
        }

        String albumName = execQuery.getAlbumName();
        if (adminBot.checkAlbumNameForUser(artist, albumName)) {
            return AddAlbumExit.Status.SAME_NAME;
        }

        if (tool.hasSameElementTwice(execQuery.getSongs())) {
            return AddAlbumExit.Status.SAME_SONG;
        }

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

        if (!adminBot.checkUsername(hostName)) {
            return AddPodcastExit.Status.DOESNT_EXIST;
        }

        User host = adminBot.getHostByUsername(hostName);
        if (host == null) {
            return AddPodcastExit.Status.NOT_HOST;
        }

        String podcastName = execQuery.getPodcastName();
        if (adminBot.checkPodcastNameForUser(host, podcastName)) {
            return AddPodcastExit.Status.SAME_NAME;
        }

        if (tool.hasSameElementTwice(execQuery.getEpisodes())) {
            return AddPodcastExit.Status.DUPLICATE;
        }

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

        if (!adminBot.checkUsername(username)) {
            return AddEventExit.Status.DOESNT_EXIST;
        }

        User artist = adminBot.getArtistByUsername(username);
        if (artist == null) {
            return AddEventExit.Status.NOT_ARTIST;
        }

        String eventName = execQuery.getName();
        String eventDescription = execQuery.getDescription();
        LocalDate eventDate = DateMapper.parseStringToDate(execQuery.getDate());

        if (artist.hasEvent(eventName)) {
            return AddEventExit.Status.SAME_NAME;
        }

        if (eventDate == null) {
            return AddEventExit.Status.INVALID_DATE;
        }

        artist.addEvent(new Event(eventName, eventDescription, eventDate));

        return AddEventExit.Status.SUCCESS;
    }

    public AddMerchExit.Status requestAddingMerch(final AddMerchInterrogator execQuery) {
        String username = execQuery.getUsername();

        if (!adminBot.checkUsername(username)) {
            return AddMerchExit.Status.DOESNT_EXIST;
        }

        User artist = adminBot.getArtistByUsername(username);
        if (artist == null) {
            return AddMerchExit.Status.NOT_ARTIST;
        }

        String merchName = execQuery.getName();
        String merchDescription = execQuery.getDescription();
        int merchPrice = execQuery.getPrice();

        if (artist.hasMerch(merchName)) {
            return AddMerchExit.Status.SAME_NAME;
        }

        if (merchPrice < 0) {
            return AddMerchExit.Status.NEGATIVE_PRICE;
        }

        artist.addMerch(new Merch(merchName, merchDescription, merchPrice));

        return AddMerchExit.Status.SUCCESS;
    }

    public RemoveEventExit.Status requestRemovingEvent(final RemoveEventInterrogator execQuery) {
        String username = execQuery.getUsername();

        if (!adminBot.checkUsername(username)) {
            return RemoveEventExit.Status.DOESNT_EXIST;
        }

        User artist = adminBot.getArtistByUsername(username);
        if (artist == null) {
            return RemoveEventExit.Status.NOT_ARTIST;
        }

        String eventName = execQuery.getName();
        Event artistEvent = artist.getEvent(eventName);
        if (artistEvent == null) {
            return RemoveEventExit.Status.INVALID_NAME;
        }

        artist.removeEvent(artistEvent);

        return RemoveEventExit.Status.SUCCESS;
    }

    public AddAnnouncementExit.Status
    requestAddingAnnouncement(final AddAnnouncementInterrogator execQuery) {
        String username = execQuery.getUsername();

        if (!adminBot.checkUsername(username)) {
            return AddAnnouncementExit.Status.DOESNT_EXIST;
        }

        User host = adminBot.getHostByUsername(username);
        if (host == null) {
            return AddAnnouncementExit.Status.NOT_HOST;
        }

        String announcementName = execQuery.getName();
        String announcementDescription = execQuery.getDescription();
        if (host.hasAnnouncement(announcementName)) {
            return AddAnnouncementExit.Status.SAME_NAME;
        }

        host.addAnnouncement(new Announcement(announcementName, announcementDescription));

        return AddAnnouncementExit.Status.SUCCESS;
    }

    public RemoveAnnouncementExit.Status
    requestRemovingAnnouncement(final RemoveAnnouncementInterrogator execQuery) {
        String username =  execQuery.getUsername();

        if (!adminBot.checkUsername(username)) {
            return RemoveAnnouncementExit.Status.DOESNT_EXIST;
        }

        User host = adminBot.getHostByUsername(username);
        if (host == null) {
            return RemoveAnnouncementExit.Status.NOT_HOST;
        }

        String announcementName = execQuery.getName();
        Announcement announcement = host.getAnnouncement(announcementName);
        if (announcement == null) {
            return RemoveAnnouncementExit.Status.INVALID_NAME;
        }

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

        // If getUserByUsername method returned null, it means that the user doesn't exist
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }

        if (userIsBeingListened(username)) {
            return username + " can't be deleted.";
        }

        if (userIsBeingWatched(username)) {
            return username + " can't be deleted.";
        }

        if (adminBot.playlistsHaveSongFromArtist(username)) {
            return username + " can't be deleted.";
        }

        if (user.isNormalUser()) {
            userInterfaces.remove(username);
        }

        adminBot.removeUser(user);

        return username + " was successfully deleted.";

    }

    public RemoveAlbumExit.Status requestRemovingAlbum(final RemoveAlbumInterrogator execQuery) {
        String username = execQuery.getUsername();
        String albumName = execQuery.getName();

        if (!adminBot.checkUsername(username)) {
            return RemoveAlbumExit.Status.DOESNT_EXIST;
        }

        User artist = adminBot.getArtistByUsername(username);
        if (artist == null) {
            return RemoveAlbumExit.Status.NOT_ARTIST;
        }

        if (!artist.hasAlbumWithName(albumName)) {
            return RemoveAlbumExit.Status.DONT_HAVE;
        }

        // Retrieve the album from artist. It won't return null,
        // because we already checked if the user is an artist, or
        // if the artist doesn't have an album with that name
        Album album = artist.getAlbumByName(albumName);

        // If there is at least one song from the album used in a playlist
        if (album.isUsedInPlaylist()) {
            return RemoveAlbumExit.Status.FAIL;
        }

        // If there is at least one player listening to something from album
        if (albumIsBeingListened(album)) {
            return RemoveAlbumExit.Status.FAIL;
        }

        // Remove the album as an admin
        adminBot.removeAlbum(album);
        return RemoveAlbumExit.Status.SUCCESS;
    }

    public RemovePodcastExit.Status
    requestRemovingPodcast(final RemovePodcastInterrogator execQuery) {
        String username = execQuery.getUsername();
        String podcastName = execQuery.getName();

        if (!adminBot.checkUsername(username)) {
            return RemovePodcastExit.Status.DOESNT_EXIST;
        }

        User host = adminBot.getHostByUsername(username);
        if (host == null) {
            return RemovePodcastExit.Status.NOT_HOST;
        }

        if (!host.hasPodcastWithName(podcastName)) {
            return RemovePodcastExit.Status.DONT_HAVE;
        }

        if (userIsBeingListened(username)) {
            return RemovePodcastExit.Status.FAIL;
        }

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