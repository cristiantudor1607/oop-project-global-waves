package globalwaves.player.entities.library;

import globalwaves.commands.enums.exitcodes.stageone.*;
import globalwaves.commands.enums.exitcodes.stagetwo.SwitchConnectionExit;
import globalwaves.commands.stageone.*;
import globalwaves.commands.stagetwo.ConnectionInterrogator;
import globalwaves.commands.stagetwo.OnlineUsersInterrogator;
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
    private SuperAdmin admin;
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
        admin = new SuperAdmin();
        searchBar = new SearchBar();
        players = new HashMap<>();
        for (User user : Library.getInstance().getUsers()) {
            players.put(user.getUsername(), new Player());
        }

        lastActionTime = 0;
    }

    public Player requestPlayer(CommandObject execQuery) {
        String username = execQuery.getUsername();

        return players.get(username);
    }

    public SearchExit.Status requestApprovalForSearch(SearchInterrogator execQuery) {
        User queriedUser = admin.getUserByUsername(execQuery.getUsername());
        if (queriedUser.isOffline())
            return SearchExit.Status.OFFLINE;

        return SearchExit.Status.SUCCESS;
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

        if (admin.checkIfOwnerHasPlaylist(owner, playlistName))
            return CreationExit.Status.ALREADY_EXISTS;

        admin.createPlaylist(owner, playlistName, timestamp);

        return CreationExit.Status.CREATED;
    }

    public SwitchVisibilityExit.Status requestSwitchVisibility(VisibilityInterrogator execQuery) {
        int id = execQuery.getPlaylistId();
        String owner = execQuery.getUsername();

        Playlist ownerPlaylist = admin.getOwnerPlaylistById(owner, id);
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
        Playlist ownerPlaylist = admin.getOwnerPlaylistById(owner, id);
        Player ownerPlayer = players.get(owner);

        if (!ownerPlayer.hasSourceLoaded())
            return  AddRemoveExit.Status.NO_SOURCE;

        if (!ownerPlayer.getPlayingFile().canBeLiked())
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
        return admin.getOwnerPlaylists(owner);
    }

    public LikeExit.Status requestLikeAction(LikeInterrogator execQuery) {
        String username = execQuery.getUsername();
        User queriedUser = admin.getUserByUsername(username);
        Player queriedUserPlayer = getPlayers().get(username);

        if (!queriedUserPlayer.hasSourceLoaded())
            return LikeExit.Status.NO_SOURCE;

        AudioFile queriedSong = queriedUserPlayer.getPlayingFile();

        if (!queriedSong.canBeLiked())
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

        List<AudioFile> songs = admin.getUserLikedSongs(username);

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
        return admin.getTopFiveSongs();
    }

    public List<String> requestTopFivePlaylists() {
        return admin.getTopFivePlaylists();
    }

    public SwitchConnectionExit.Code requestSwitchConnection(ConnectionInterrogator execQuery) {
        User queriedUser = admin.getUserByUsername(execQuery.getUsername());

        if (queriedUser == null)
            return SwitchConnectionExit.Code.INVALID_USERNAME;

        if (!queriedUser.isNormalUser())
            return SwitchConnectionExit.Code.NOT_NORMAL;

        Player userPlayer = requestPlayer(execQuery);
        if (userPlayer.isFreeze())
            userPlayer.unfreeze();
        else
            userPlayer.freeze();
        queriedUser.switchStatus();
        return SwitchConnectionExit.Code.SUCCESS;
    }

    public List<String> requestOnlineUsers(OnlineUsersInterrogator execQuery) {
        List<User> onlineUsers = admin.getOnlineUsers();

        return admin.getUsernames(onlineUsers);
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
