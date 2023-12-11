package globalwaves.player.entities.library;

import globalwaves.commands.enums.UserType;
import globalwaves.commands.enums.exitstats.stageone.*;
import globalwaves.commands.enums.exitstats.stagetwo.AddAlbumExit;
import globalwaves.commands.enums.exitstats.stagetwo.AddUserExit;
import globalwaves.commands.enums.exitstats.stagetwo.SwitchConnectionExit;
import globalwaves.commands.stageone.*;
import globalwaves.commands.stagetwo.*;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.*;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter @Setter
public class ActionManager {
    private static ActionManager instance;
    private AdminBot adminBot;
    private HelperTool tool;
    private int lastActionTime;

    private Map<String, UserInterface> userInterfaces;

    public static ActionManager getInstance() {
        if (instance == null)
            instance = new ActionManager();

        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }


    private ActionManager() {
        adminBot = new AdminBot();
        tool = HelperTool.getInstance();
        lastActionTime = 0;

        userInterfaces = new HashMap<>();
        for (User user: adminBot.getDatabase().getUsers()) {
            userInterfaces.put(user.getUsername(), new UserInterface(user));
        }
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

    public Player requestPlayer(CommandObject execQuery) {
        String username = execQuery.getUsername();

        return getPlayerByUsername(username);
    }

    public boolean requestApprovalForAction(CommandObject execQuery) {
        User queriedUser = adminBot.getUserByUsername(execQuery.getUsername());

        return queriedUser.isOnline();
    }

    public List<String> requestSearchResults(SearchInterrogator execQuery) {
        String username = execQuery.getUsername();

        Player userPlayer = getPlayerByUsername(username);
        SearchBar searchbar = getSearchBarByUsername(username);

        userPlayer.stopPlayer();
        searchbar.search(execQuery.getType(), execQuery.getFilters());

        return searchbar.getRelevantResultsAsNames();
    }

    public SelectExit.Status requestItemSelection(SelectInterrogator executingSelect) {
        int itemNumber = executingSelect.getItemNumber();
        String username = executingSelect.getUsername();

        Player userPlayer = getPlayerByUsername(username);
        SearchBar searchbar = getSearchBarByUsername(username);

        if (searchbar.wasNotInvoked())
            return SelectExit.Status.NO_LIST;

        if (!searchbar.invalidItem(itemNumber) || searchbar.hasNoSearchResult())
            return SelectExit.Status.OUT_OF_BOUNDS;

        PlayableEntity entity = searchbar.getResultAtIndex(itemNumber - 1);

        userPlayer.select(entity);
        executingSelect.setSelectedAudio(entity);

        // Reset the Search Bar after selection
        searchbar.reset();

        return SelectExit.Status.SELECTED;
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

        userPlayer.load();

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
        String owner = execQuery.getUsername();
        int id = execQuery.getPlaylistId();

        Player ownerPlayer = getPlayerByUsername(owner);
        Playlist ownerPlaylist = adminBot.getOwnerPlaylistById(owner, id);

        if (!ownerPlayer.hasSourceLoaded())
            return  AddRemoveExit.Status.NO_SOURCE;

        if (!ownerPlayer.getPlayingFile().isSong())
            return AddRemoveExit.Status.NOT_A_SONG;

        if (ownerPlaylist == null)
            return AddRemoveExit.Status.INVALID_PLAYLIST;

        AudioFile selectedSource = ownerPlayer.getPlayingFile();
        if (ownerPlaylist.hasSong(selectedSource)) {
            ownerPlaylist.removeSong(selectedSource);
            return AddRemoveExit.Status.REMOVED;
        } else {
            ownerPlaylist.addSong(selectedSource);
            return AddRemoveExit.Status.ADDED;
        }
    }

    public List<Playlist> requestOwnerPlaylists(final String owner) {
        return adminBot.getOwnerPlaylists(owner);
    }

    public LikeExit.Status requestLikeAction(LikeInterrogator execQuery) {
        String username = execQuery.getUsername();

        User queriedUser = getProfileByUsername(username);
        Player queriedPlayer = getPlayerByUsername(username);

        if (!queriedPlayer.hasSourceLoaded())
            return LikeExit.Status.NO_SOURCE;

        AudioFile queriedSong = queriedPlayer.getPlayingFile();

        if (!queriedSong.isSong())
            return LikeExit.Status.NOT_A_SONG;

        if (queriedUser.hasLikedSong(queriedSong)) {
            queriedUser.removeSongFromLikes(queriedSong);
            return LikeExit.Status.UNLIKED;
        } else {
            queriedUser.addSongToLikes(queriedSong);
            return LikeExit.Status.LIKED;
        }
    }

    public List<String> requestLikedSongs(ShowLikesInterrogator execQuery) {
        String username = execQuery.getUsername();

        List<AudioFile> songs = adminBot.getUserLikedSongs(username);

        List<String> names = new ArrayList<>();
        for (AudioFile file : songs)
            names.add(file.getName());

        return names;
    }

    public FollowExit.Status requestFollowAction(FollowInterrogator execQuery) {
        String username = execQuery.getUsername();

        User user = getProfileByUsername(username);
        Player userPlayer = getPlayerByUsername(username);

        // TODO: Aici trebuie adaugat playlistul la follow la user

        if (userPlayer.hasNoSource())
            return FollowExit.Status.NO_SOURCE;

        return userPlayer.getSelectedSource().follow(username);
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
        if (userPlayer.isShuffle()) {
            userPlayer.setShuffle(false);
            shuffleExit = userPlayer.getSelectedSource().unshuffle();
            userPlayer.changeOrderAfterShuffle();
        } else {
            userPlayer.setShuffle(true);
            shuffleExit = userPlayer.getSelectedSource().shuffle(seed);
            userPlayer.changeOrderAfterShuffle();
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

        userPlayer.skipForward();
        return "Skipped forward successfully.";
    }

    public String requestBackward(BackwardInterrogator execQuery) {
        String username = execQuery.getUsername();
        Player userPlayer = getPlayerByUsername(username);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before rewinding.";

        if (userPlayer.getSelectedSource().cantGoForwardOrBackward())
            return "The loaded source is not a podcast.";

        userPlayer.rewoundBackward();
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
        return AddUserExit.Status.SUCCESS;
    }

    public List<String> requestOnlineUsers(OnlineUsersInterrogator execQuery) {
        List<User> onlineUsers = adminBot.getOnlineUsers();

        return tool.getUsernames(onlineUsers);
    }

    public AddAlbumExit.Status requestAddingAlbum(AddAlbumInterrogator execQuery) {
        boolean usernameExist = adminBot.checkUsername(execQuery.getUsername());
        if (!usernameExist)
            return AddAlbumExit.Status.INVALID_USERNAME;

        User u = adminBot.getArtistByUsername(execQuery.getUsername());
        if (!u.isArtist())
            return AddAlbumExit.Status.NOT_ARTIST;

        String albumName = execQuery.getAlbumName();
        if (adminBot.checkAlbumNameForUser(u, albumName))
            return AddAlbumExit.Status.SAME_NAME;

        if (tool.hasSameSongAtLeastTwice(execQuery.getSongs()))
            return AddAlbumExit.Status.SAME_SONG;

        String artistName = execQuery.getUsername();
        int creationTime = execQuery.getTimestamp();

        // Set the creation time for all songs on the album
        tool.setCreationTimestamp(execQuery.getSongs(), creationTime);
        // Add new songs to library
        adminBot.addSongsToLibrary(artistName, execQuery.getSongs());
        // Create the album object
        Album artistNewAlbum = new Album(artistName, albumName, creationTime, execQuery.getSongs());
        // Add album to user albums
        u.addAlbum(artistNewAlbum);

        return AddAlbumExit.Status.SUCCESS;
    }

    public List<Album> requestUserAlbums(final ShowAlbumsInterrogator execQuery) {
        String username = execQuery.getUsername();

        return adminBot.getArtistAlbums(username);
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
