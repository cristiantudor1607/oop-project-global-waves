package globalwaves.player.entities.utilities;

import globalwaves.player.entities.Song;

import java.util.Comparator;
import java.util.Map;

public class SortByInteger implements Comparator<Map.Entry<Song, Integer>> {
    @Override
    public int compare(Map.Entry<Song, Integer> o1, Map.Entry<Song, Integer> o2) {
        return o2.getValue() - o1.getValue();
    }
}
