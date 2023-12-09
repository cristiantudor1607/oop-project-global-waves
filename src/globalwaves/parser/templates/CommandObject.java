package globalwaves.parser.templates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.AddRemoveInterrogator;
import globalwaves.commands.BackwardInterrogator;
import globalwaves.commands.CreatePlaylistInterrogator;
import globalwaves.commands.FollowInterrogator;
import globalwaves.commands.ForwardInterrogator;
import globalwaves.commands.LikeInterrogator;
import globalwaves.commands.LoadInterrogator;
import globalwaves.commands.NextInterrogator;
import globalwaves.commands.PlayPauseInterrogator;
import globalwaves.commands.PrevInterrogator;
import globalwaves.commands.RepeatInterrogator;
import globalwaves.commands.SearchInterrogator;
import globalwaves.commands.SelectInterrogator;
import globalwaves.commands.ShowLikesInterrogator;
import globalwaves.commands.ShowPlaylistsInterrogator;
import globalwaves.commands.ShuffleInterrogator;
import globalwaves.commands.StatusInterrogator;
import globalwaves.commands.SwitchVisibilityInterrogator;
import globalwaves.commands.TopFivePlaylistsInterrogator;
import globalwaves.commands.TopFiveSongsInterrogator;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "command")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SearchInterrogator.class, name = "search"),
        @JsonSubTypes.Type(value = SelectInterrogator.class, name = "select"),
        @JsonSubTypes.Type(value = LoadInterrogator.class, name = "load"),
        @JsonSubTypes.Type(value = PlayPauseInterrogator.class, name = "playPause"),
        @JsonSubTypes.Type(value = RepeatInterrogator.class, name = "repeat"),
        @JsonSubTypes.Type(value = ShuffleInterrogator.class, name = "shuffle"),
        @JsonSubTypes.Type(value = ForwardInterrogator.class, name = "forward"),
        @JsonSubTypes.Type(value = BackwardInterrogator.class, name = "backward"),
        @JsonSubTypes.Type(value = LikeInterrogator.class, name = "like"),
        @JsonSubTypes.Type(value = NextInterrogator.class, name = "next"),
        @JsonSubTypes.Type(value = PrevInterrogator.class, name = "prev"),
        @JsonSubTypes.Type(value = AddRemoveInterrogator.class, name = "addRemoveInPlaylist"),
        @JsonSubTypes.Type(value = StatusInterrogator.class, name = "status"),
        @JsonSubTypes.Type(value = CreatePlaylistInterrogator.class, name = "createPlaylist"),
        @JsonSubTypes.Type(value = SwitchVisibilityInterrogator.class, name = "switchVisibility"),
        @JsonSubTypes.Type(value = FollowInterrogator.class, name = "follow"),
        @JsonSubTypes.Type(value = ShowPlaylistsInterrogator.class, name = "showPlaylists"),
        @JsonSubTypes.Type(value = ShowLikesInterrogator.class, name = "showPreferredSongs"),
        @JsonSubTypes.Type(value = TopFiveSongsInterrogator.class, name = "getTop5Songs"),
        @JsonSubTypes.Type(value = TopFivePlaylistsInterrogator.class, name = "getTop5Playlists"),

})
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public abstract class CommandObject {
    protected String username;
    protected Integer timestamp;

    /**
     * The execute method is used to execute a command and get is output as JsonNode
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode
     */
    public JsonNode execute(final ActionManager manager) {
        return null;
    }

    /**
     * Method that checks if the executing command has to work with filters.
     * @return true, if it has to work with filters, false otherwise
     */
    public boolean hasFilters() {
        return false;
    }

    /**
     * Method used to setFilters, if the command requires them. Otherwise, it does nothing.
     * @param mappedFilters The filters parsed as a Map.
     */
    public void setFilters(final Map<String, List<String>> mappedFilters) { }

}
