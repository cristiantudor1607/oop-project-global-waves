package globalwaves.player.entities.library;

import globalwaves.commands.*;
import globalwaves.commands.enums.*;
import globalwaves.commands.search.utils.EngineResultsParser;
import globalwaves.parser.commands.CommandObject;
import globalwaves.player.entities.*;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter @Setter
public class ActionManager {
    private LibraryInterrogator interrogator;
    private SearchBar searchBar;
    private Map<String, Player> players;
    private int lastActionTime;

    public ActionManager() {
        interrogator = new LibraryInterrogator();
        searchBar = new SearchBar();
        players = new HashMap<>();
        for (User user : Library.getInstance().getUsers()) {
            players.put(user.getUsername(), new Player());
        }

        lastActionTime = 0;
    }

    public Player requestPlayer(CommandObject executingQuery) {
        String username = executingQuery.getUsername();

        return players.get(username);
    }

    public void requestSearchResult(SearchInterrogator executingSearch) {
        // Reset the player
        Player userPlayer = requestPlayer(executingSearch);
        userPlayer.resetPlayer();

        searchBar.setResults(executingSearch.getSearchResults());
        searchBar.setUsername(executingSearch.getUsername());
    }

    public SelectExit.code requestItemSelection(SelectInterrogator executingSelect) {
        int itemNumber = executingSelect.getItemNumber();

        if (!searchBar.hasSearchResults())
            return SelectExit.code.NO_LIST;

        if (!searchBar.invalidItem(itemNumber))
            return SelectExit.code.OUT_OF_BOUNDS;

        PlayableEntity entity = searchBar.getResultAtIndex(itemNumber - 1);
        Player userPlayer = players.get(executingSelect.getUsername());
        /*
        Set the player state
         */
        userPlayer.setSelectedAudio(entity);
        userPlayer.setState(Player.PlayerStatus.SELECTED);
        /*
        Send the entity to the command, so it can generate the output
         */
        executingSelect.setSelectedAudio(entity);

        // Reset the Search Bar after selection
        searchBar.reset();

        return SelectExit.code.SELECTED;
    }

    public LoadExit.code requestLoading(LoadInterrogator executingLoad) {
        Player userPlayer = players.get(executingLoad.getUsername());

        if (!userPlayer.hasSourceSelected())
            return LoadExit.code.NO_SOURCE_SELECTED;

        if (userPlayer.hasEmptySource())
            return LoadExit.code.EMPTY_SOURCE;

        // O sa mai adaug ceva aici cu siguranta
        userPlayer.setState(Player.PlayerStatus.PLAYING);
        userPlayer.setRepeat(Player.RepeatValue.NO_REPEAT);
        userPlayer.setShuffle(false);
        userPlayer.setRemainedTime(userPlayer.getSelectedAudio().getDuration());

        return LoadExit.code.LOADED;
    }

    public PlayPauseExit.code requestUpdateState(PlayPauseInterrogator executingQuery) {
        Player userPlayer = players.get(executingQuery.getUsername());

        if (!userPlayer.hasSourceSelected())
            return PlayPauseExit.code.NO_SOURCE;

        if (userPlayer.isPlaying()) {
            userPlayer.setState(Player.PlayerStatus.PAUSED);
            return PlayPauseExit.code.PAUSED;
        }

        userPlayer.setState(Player.PlayerStatus.PLAYING);

        return PlayPauseExit.code.RESUMED;
    }

    public CreationExit.code requestPlaylistCreation(CreatePlaylistInterrogator execQuery) {
        String owner = execQuery.getUsername();
        String playlistName = execQuery.getPlaylistName();

        if (interrogator.ownerHasPlaylist(owner, playlistName))
            return CreationExit.code.ALREADY_EXISTS;

        interrogator.createPlaylist(owner, playlistName);
        return CreationExit.code.CREATED;
    }

    public SwitchVisibilityExit.code requestSwitch(SwitchVisibilityInterrogator execQuery) {
        int id = execQuery.getPlaylistId();
        String owner = execQuery.getUsername();

        Playlist ownerPlaylist = interrogator.getOwnerPlaylistById(owner, id);
        if (ownerPlaylist == null)
            return SwitchVisibilityExit.code.TOO_HIGH;

        if (ownerPlaylist.isPublic()) {
            ownerPlaylist.makePrivate();
            return SwitchVisibilityExit.code.MADE_PRIVATE;
        }

        ownerPlaylist.makePublic();
        return SwitchVisibilityExit.code.MADE_PUBLIC;
    }

    public AddRemoveExit.code requestAddRemove(AddRemoveInterrogator execQuery) {
        String owner = execQuery.getUsername();
        int id = execQuery.getPlaylistId();
        Playlist ownerPlaylist = interrogator.getOwnerPlaylistById(owner, id);
        Player ownerPlayer = players.get(owner);

        if (!ownerPlayer.hasSourceLoaded())
            return  AddRemoveExit.code.NO_SOURCE;

        if (!ownerPlayer.getSelectedAudio().isSong())
            return AddRemoveExit.code.NOT_A_SONG;

        if (ownerPlaylist == null)
            return AddRemoveExit.code.INVALID_PLAYLIST;

        PlayableEntity selectedSource = ownerPlayer.getSelectedAudio();
        if (ownerPlaylist.hasSong((Song) selectedSource)) {
            ownerPlaylist.removeSong((Song) selectedSource);
            return AddRemoveExit.code.REMOVED;
        } else {
            ownerPlaylist.addSong((Song) selectedSource);
            return AddRemoveExit.code.ADDED;
        }
    }

    public List<Playlist> requestOwnerPlaylists(final String owner) {
        return interrogator.getOwnerPlaylists(owner);
    }

    public LikeExit.code requestLikeAction(LikeInterrogator execQuery) {
        // TODO : Add the case when there is a playlist

        String username = execQuery.getUsername();
        User queriedUser = interrogator.getUserByUsername(username);
        Player queriedUserPlayer = getPlayers().get(username);

        if (!queriedUserPlayer.hasSourceLoaded())
            return LikeExit.code.NO_SOURCE;

        PlayableEntity queriedSong = queriedUserPlayer.getSelectedAudio();

        if (!queriedSong.isSong())
            return LikeExit.code.NOT_A_SONG;

        if (queriedUser.hasLikedSong((Song) queriedSong)) {
            queriedUser.removeSongFromLikes((Song) queriedSong);
            return LikeExit.code.UNLIKED;
        } else {
            queriedUser.addSongToLikes((Song) queriedSong);
            return LikeExit.code.LIKED;
        }
    }

    public List<String> requestLikedSongs(ShowLikesInterrogator execQuery) {
        String username = execQuery.getUsername();

        List<Song> likedSongs = interrogator.getUserLikedSongs(username);

        return EngineResultsParser.getNamesFromList(likedSongs);
    }

    public void updatePlayersData(CommandObject nextToExecuteCommand) {
        int diff = nextToExecuteCommand.getTimestamp() - lastActionTime;
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Player currPlayer = entry.getValue();
            currPlayer.updatePlayer(diff);
        }
    }

}
