package globalwaves.player.entities;

import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SearchBar {
    private String username;
    private List<? extends PlayableEntity> results;

    public SearchBar() {
        username = null;
        results = new ArrayList<>();
    }

    public boolean hasSearchResults() {
        if (results == null)
            return false;

        return !results.isEmpty() && username != null;
    }

    public boolean invalidItem(int index) {
        return username != null && index <= results.size();
    }

    public void reset() {
        username = null;
        results = null;
    }

    public boolean isEmpty() {
        return username == null;
    }

    public PlayableEntity getResultAtIndex(int index) {
        return results.get(index);
    }
}
