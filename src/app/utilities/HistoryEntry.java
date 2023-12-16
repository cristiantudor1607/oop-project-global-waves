package app.utilities;

import app.player.entities.AudioFile;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HistoryEntry {
    private AudioFile file;
    private int remainedTime;

    public HistoryEntry(AudioFile file, int remainedTime) {
        this.file = file;
        this.remainedTime = remainedTime;
    }
}
