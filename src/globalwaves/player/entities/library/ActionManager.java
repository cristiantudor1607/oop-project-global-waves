package globalwaves.player.entities.library;

import globalwaves.commands.*;
import globalwaves.commands.enums.exitcodes.*;
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

    private LibraryInterrogator interrogator;
    private SearchBar searchBar;
    private Map<String, Player> players;
    private int lastActionTime;

    public static ActionManager getInstance() {
        if (instance == null)
            instance = new ActionManager();

        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }


    private ActionManager() {
        interrogator = new LibraryInterrogator();
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

    public List<String> requestSearch(SearchInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);
        userPlayer.stopPlayer();

        searchBar.setUsername(execQuery.getUsername());
        searchBar.search(execQuery.getType(), execQuery.getFilters());

        return searchBar.getRelevantResultsAsNames();
    }

    public void requestSearchResult(SearchInterrogator executingSearch) {
        // First stop the player
        Player userPlayer = requestPlayer(executingSearch);
        userPlayer.stopPlayer();
    }

    public SelectExit.Code requestItemSelection(SelectInterrogator executingSelect) {
        int itemNumber = executingSelect.getItemNumber();

        if (searchBar.wasNotInvoked())
            return SelectExit.Code.NO_LIST;

        if (!searchBar.invalidItem(itemNumber) || searchBar.hasNoSearchResult())
            return SelectExit.Code.OUT_OF_BOUNDS;

        PlayableEntity entity = searchBar.getResultAtIndex(itemNumber - 1);
        Player userPlayer = players.get(executingSelect.getUsername());
        userPlayer.select(entity);
        executingSelect.setSelectedAudio(entity);

        // Reset the Search Bar after selection
        searchBar.reset();

        return SelectExit.Code.SELECTED;
    }

    public LoadExit.Code requestLoading(LoadInterrogator executingLoad) {
        Player userPlayer = players.get(executingLoad.getUsername());

        if (!userPlayer.hasSourceSelected())
            return LoadExit.Code.NO_SOURCE_SELECTED;

        if (userPlayer.hasNoSource())
            return LoadExit.Code.NO_SOURCE_SELECTED;

        if (userPlayer.hasEmptySource())
            return LoadExit.Code.EMPTY_SOURCE;

        userPlayer.load();

        return LoadExit.Code.LOADED;
    }

    public PlayPauseExit.Code requestUpdateState(PlayPauseInterrogator executingQuery) {
        Player userPlayer = players.get(executingQuery.getUsername());

        if (userPlayer.hasNoSource())
            return PlayPauseExit.Code.NO_SOURCE;

        if (userPlayer.isPlaying()) {
            userPlayer.pause();
            return PlayPauseExit.Code.PAUSED;
        }

        userPlayer.play();

        return PlayPauseExit.Code.RESUMED;
    }

    public CreationExit.Code requestPlaylistCreation(CreatePlaylistInterrogator execQuery) {
        String owner = execQuery.getUsername();
        String playlistName = execQuery.getPlaylistName();
        int timestamp = execQuery.getTimestamp();

        if (interrogator.ownerHasPlaylist(owner, playlistName))
            return CreationExit.Code.ALREADY_EXISTS;

        interrogator.createPlaylist(owner, playlistName, timestamp);

        return CreationExit.Code.CREATED;
    }

    public SwitchVisibilityExit.Code requestSwitch(SwitchVisibilityInterrogator execQuery) {
        int id = execQuery.getPlaylistId();
        String owner = execQuery.getUsername();

        Playlist ownerPlaylist = interrogator.getOwnerPlaylistById(owner, id);
        if (ownerPlaylist == null)
            return SwitchVisibilityExit.Code.TOO_HIGH;

        if (ownerPlaylist.isPublic()) {
            ownerPlaylist.makePrivate();
            return SwitchVisibilityExit.Code.MADE_PRIVATE;
        }

        ownerPlaylist.makePublic();

        return SwitchVisibilityExit.Code.MADE_PUBLIC;
    }

    public AddRemoveExit.Code requestAddRemove(AddRemoveInterrogator execQuery) {
        String owner = execQuery.getUsername();
        int id = execQuery.getPlaylistId();
        Playlist ownerPlaylist = interrogator.getOwnerPlaylistById(owner, id);
        Player ownerPlayer = players.get(owner);

        if (!ownerPlayer.hasSourceLoaded())
            return  AddRemoveExit.Code.NO_SOURCE;

        if (!ownerPlayer.getPlayingFile().canBeLiked())
            return AddRemoveExit.Code.NOT_A_SONG;

        if (ownerPlaylist == null)
            return AddRemoveExit.Code.INVALID_PLAYLIST;

        AudioFile selectedSource = ownerPlayer.getPlayingFile();
        if (ownerPlaylist.hasSong(selectedSource)) {
            ownerPlaylist.removeSong(selectedSource);
            return AddRemoveExit.Code.REMOVED;
        } else {
            ownerPlaylist.addSong(selectedSource);
            return AddRemoveExit.Code.ADDED;
        }
    }

    public List<Playlist> requestOwnerPlaylists(final String owner) {
        return interrogator.getOwnerPlaylists(owner);
    }

    public LikeExit.Code requestLikeAction(LikeInterrogator execQuery) {
        String username = execQuery.getUsername();
        User queriedUser = interrogator.getUserByUsername(username);
        Player queriedUserPlayer = getPlayers().get(username);

        if (!queriedUserPlayer.hasSourceLoaded())
            return LikeExit.Code.NO_SOURCE;

        AudioFile queriedSong = queriedUserPlayer.getPlayingFile();

        if (!queriedSong.canBeLiked())
            return LikeExit.Code.NOT_A_SONG;

        if (queriedUser.hasLikedSong(queriedSong)) {
            queriedUser.removeSongFromLikes(queriedSong);
            return LikeExit.Code.UNLIKED;
        } else {
            queriedUser.addSongToLikes(queriedSong);
            return LikeExit.Code.LIKED;
        }
    }

    public List<String> requestLikedSongs(ShowLikesInterrogator execQuery) {
        String username = execQuery.getUsername();

        List<AudioFile> songs = interrogator.getUserLikedSongs(username);

        List<String> names = new ArrayList<>();
        for (AudioFile file : songs)
            names.add(file.getName());

        return names;
    }

    public FollowExit.Code requestFollowAction(FollowInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);
        String username = execQuery.getUsername();

        if (userPlayer.hasNoSource())
            return FollowExit.Code.NO_SOURCE;

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

    public ShuffleExit.Code requestShuffling(ShuffleInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);
        int seed = execQuery.getSeed();

        if (!userPlayer.hasSourceLoaded() || userPlayer.hasNoSource())
            return ShuffleExit.Code.NO_SOURCE_LOADED;

        ShuffleExit.Code shuffleExit;
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
        return interrogator.getTopFiveSongs();
    }

    public List<String> requestTopFivePlaylists() {
        return interrogator.getTopFivePlaylists();
    }

    public void updatePlayersData(CommandObject nextToExecuteCommand) {
        int diff = nextToExecuteCommand.getTimestamp() - lastActionTime;
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Player currPlayer = entry.getValue();
            currPlayer.updatePlayer(diff);
        }
    }
}
