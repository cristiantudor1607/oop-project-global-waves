package globalwaves.player.entities;

import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SearchBar {

    private List<? extends PlayableEntity> results;

    public SearchBar() {
        results = new ArrayList<>();
    }

    public boolean hasNoSearchResult() {
        return results.isEmpty();
    }

    public boolean invalidItem(int index) {
        return  index <= results.size();
    }

    public void reset() {
        results = null;
    }

    public boolean wasNotInvoked() {
        return results == null;
    }

    public PlayableEntity getResultAtIndex(int index) {
        return results.get(index);
    }
}
