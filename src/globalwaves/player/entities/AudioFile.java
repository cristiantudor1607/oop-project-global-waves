package globalwaves.player.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AudioFile {
    protected String name;
    protected int duration;

    /**
     * Checks if the inherited class can be liked
     * @return true, if it can be liked, false, otherwise
     */
    public abstract boolean canBeLiked();
}
