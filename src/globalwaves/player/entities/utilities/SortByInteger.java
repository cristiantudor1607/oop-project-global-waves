package globalwaves.player.entities.utilities;

import globalwaves.player.entities.AudioFile;

import java.util.Comparator;
import java.util.Map;

public class SortByInteger implements Comparator<Map.Entry<AudioFile, Integer>> {
    @Override
    public int compare(Map.Entry<AudioFile, Integer> o1, Map.Entry<AudioFile, Integer> o2) {
        return o2.getValue() - o1.getValue();
    }
}
