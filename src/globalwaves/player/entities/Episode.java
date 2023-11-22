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

    @Override
    public boolean canBeLiked() {
        return false;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "\nname='" + name + '\'' +
                "\nduration=" + duration +
                "\ndescription='" + description + '\'' +
                '}';
    }

}
