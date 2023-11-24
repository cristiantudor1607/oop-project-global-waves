package globalwaves.parser.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.*;
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
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "getTop5Playlists"),

})
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public abstract class CommandObject {
    protected String username;
    protected Integer timestamp;

    public JsonNode execute(ActionManager manager) {
        System.out.println("Not implemented yet");
        return null;
    }

    public boolean isSelectAction() {
        return false;
    }

    public boolean hasFilters() {
        return false;
    }

    public Map<String, List<String>> getFilters() {
        return null;
    }

    public void setFilters(Map<String, List<String>> mappedFilters) {}

}
