package globalwaves.player.entities;

import fileio.input.EpisodeInput;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Episode extends AudioFile {
    private String description;

    public Episode(EpisodeInput input) {
        name = input.getName();
        duration = input.getDuration();
        description = input.getDescription();
    }

    /**
     * Checks if the current instance of the class can be liked
     * @return false
     */
    @Override
    public boolean canBeLiked() {
        return false;
    }

}
