package globalwaves.player.entities;

import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AudioFile implements PlayableEntity {
    protected String name;
    protected int duration;

    @Override
    public boolean isEmptyPlayableFile() {
        return false;
    }

    @Override
    public boolean isPlaylist() {
        return false;
    }
}
