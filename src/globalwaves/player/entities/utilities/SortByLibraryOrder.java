package globalwaves.player.entities.utilities;

import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.library.Library;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SortByLibraryOrder implements Comparator<Map.Entry<AudioFile, Integer>> {
    private List<AudioFile> songsInOrder;

    public SortByLibraryOrder() {
        songsInOrder = new ArrayList<>();

        for (Song s : Library.getInstance().getPreLoadedSongs())
            songsInOrder.add(s);
    }

    @Override
    public int compare(Map.Entry<AudioFile, Integer> o1, Map.Entry<AudioFile, Integer> o2) {
        if (!o1.getValue().equals(o2.getValue()))
            return 0;

        int index1 = songsInOrder.indexOf(o1.getKey());
        int index2 = songsInOrder.indexOf(o2.getKey());

        return index1 - index2;
    }
}
