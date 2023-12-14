package globalwaves.player.entities.utilities;

import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.library.Library;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SortByLibraryOrder implements Comparator<Map.Entry<Song, Integer>> {
    private final List<Song> songsInOrder;

    public SortByLibraryOrder() {
        songsInOrder = new ArrayList<>();

        songsInOrder.addAll(Library.getInstance().getSongs());
        Library.getInstance().getAddedSongs()
                .forEach(((s, songs) -> songsInOrder.addAll(songs)));

        songsInOrder.sort(new SortByTimestamp());
    }

    @Override
    public int compare(Map.Entry<Song, Integer> o1, Map.Entry<Song, Integer> o2) {
        if (!o1.getValue().equals(o2.getValue()))
            return 0;

        int index1 = songsInOrder.indexOf(o1.getKey());
        int index2 = songsInOrder.indexOf(o2.getKey());

        return index1 - index2;
    }
}
