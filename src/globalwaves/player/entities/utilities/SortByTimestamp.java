package globalwaves.player.entities.utilities;

import globalwaves.player.entities.properties.PlayableEntity;

import java.util.Comparator;

public class SortByTimestamp implements Comparator<PlayableEntity> {

    @Override
    public int compare(PlayableEntity o1, PlayableEntity o2) {
        return o1.getCreationTime() - o2.getCreationTime();
    }
}
