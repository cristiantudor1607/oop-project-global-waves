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
    private SearchBar searchBar;
    private int lastActionTime;

    private Map<String, Player> players;
    private List<User> activeUsers;

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
        searchBar = new SearchBar();
        players = new HashMap<>();
        for (User user : adminBot.getDatabase().getUsers()) {
            players.put(user.getUsername(), new Player());
        }

        lastActionTime = 0;
    }

    public Player requestPlayer(CommandObject execQuery) {
        String username = execQuery.getUsername();

        return players.get(username);
    }

    public boolean requestApprovalForAction(CommandObject execQuery) {
        User queriedUser = adminBot.getUserByUsername(execQuery.getUsername());

        return queriedUser.isOnline();
    }

    public List<String> requestSearchResults(SearchInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);
        userPlayer.stopPlayer();

        searchBar.setUsername(execQuery.getUsername());
        searchBar.search(execQuery.getType(), execQuery.getFilters());

        return searchBar.getRelevantResultsAsNames();
    }

    public SelectExit.Status requestItemSelection(SelectInterrogator executingSelect) {
        int itemNumber = executingSelect.getItemNumber();

        if (searchBar.wasNotInvoked())
            return SelectExit.Status.NO_LIST;

        if (!searchBar.invalidItem(itemNumber) || searchBar.hasNoSearchResult())
            return SelectExit.Status.OUT_OF_BOUNDS;

        PlayableEntity entity = searchBar.getResultAtIndex(itemNumber - 1);
        Player userPlayer = players.get(executingSelect.getUsername());
        userPlayer.select(entity);
        executingSelect.setSelectedAudio(entity);

        // Reset the Search Bar after selection
        searchBar.reset();

        return SelectExit.Status.SELECTED;
    }

    public LoadExit.Status requestLoading(LoadInterrogator executingLoad) {
        Player userPlayer = players.get(executingLoad.getUsername());

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
        Player userPlayer = players.get(executingQuery.getUsername());

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
        Playlist ownerPlaylist = adminBot.getOwnerPlaylistById(owner, id);
        Player ownerPlayer = players.get(owner);

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
        User queriedUser = adminBot.getUserByUsername(username);
        Player queriedUserPlayer = getPlayers().get(username);

        if (!queriedUserPlayer.hasSourceLoaded())
            return LikeExit.Status.NO_SOURCE;

        AudioFile queriedSong = queriedUserPlayer.getPlayingFile();

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
        Player userPlayer = requestPlayer(execQuery);
        String username = execQuery.getUsername();

        if (userPlayer.hasNoSource())
            return FollowExit.Status.NO_SOURCE;

        return userPlayer.getSelectedSource().follow(username);
    }

    public String requestRepeatAction(RepeatInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);

        if (!userPlayer.hasSourceLoaded() || userPlayer.hasNoSource())
            return "Please load a source before setting the repeat status.";

        userPlayer.changeRepeatState();
        int newRepeat = userPlayer.getRepeat();
        String stateName = userPlayer.getSelectedSource().getRepeatStateName(newRepeat);
        return "Repeat mode changed to " + stateName.toLowerCase() + ".";
    }

    public ShuffleExit.Status requestShuffling(ShuffleInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);
        int seed = execQuery.getSeed();

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
       Player userPlayer = requestPlayer(execQuery);

       if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
           return "Please load a source before skipping to the next track.";

       if (userPlayer.playNext()) {
           String track = userPlayer.getPlayingFile().getName();
           return "Skipped to next track successfully. The current track is " + track + ".";
       }

        return "Please load a source before skipping to the next track.";
    }

    public String requestPrev(PrevInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);


        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before returning to the previous track.";

        userPlayer.playPrev(execQuery.getTimestamp() - lastActionTime);
        return "Returned to previous track successfully. The current track is " +
                userPlayer.getPlayingFile().getName() + ".";
    }

    public String requestForward(ForwardInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before attempting to forward.";

        if (userPlayer.getSelectedSource().cantGoForwardOrBackward())
            return "The loaded source is not a podcast.";

        userPlayer.skipForward();
        return "Skipped forward successfully.";
    }

    public String requestBackward(BackwardInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);

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
        User queriedUser = adminBot.getUserByUsername(execQuery.getUsername());

        if (queriedUser == null)
            return SwitchConnectionExit.Status.INVALID_USERNAME;

        if (!queriedUser.isNormalUser())
            return SwitchConnectionExit.Status.NOT_NORMAL;

        Player userPlayer = requestPlayer(execQuery);
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

        Album artistNewAlbum = new Album(artistName, albumName, creationTime, execQuery.getSongs());

        // addAlbum won't fail, because of the previous checking we made
        u.addAlbum(artistNewAlbum);

        return AddAlbumExit.Status.SUCCESS;
    }

    public List<Album> requestUserAlbums(final ShowAlbumsInterrogator execQuery) {
        String username = execQuery.getUsername();

        return adminBot.getArtistAlbums(username);
    }

    public void updatePlayersData(CommandObject nextToExecuteCommand) {
        int diff = nextToExecuteCommand.getTimestamp() - lastActionTime;
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Player currPlayer = entry.getValue();
            if (currPlayer.isFreeze())
                continue;

            currPlayer.updatePlayer(diff);
        }
    }
}
