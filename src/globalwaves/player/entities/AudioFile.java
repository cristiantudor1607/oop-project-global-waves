package globalwaves.player.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AudioFile {
    protected String name;
    protected int duration;

    abstract public boolean canBeLiked();
}
