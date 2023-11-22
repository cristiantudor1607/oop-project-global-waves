package globalwaves.commands;

import globalwaves.player.entities.Player;
import lombok.Getter;

@Getter
public class PlayerStats {
    private final String name;
    private final int remainedTime;
    private String repeat;
    private final boolean shuffle;
    private final boolean paused;

    public PlayerStats(Player userPlayer) {
        if (userPlayer.getSelectedEntity() == null) {
            name = "";
            remainedTime = 0;
            repeat = "No Repeat";
            shuffle = false;
            paused = true;
        } else {
            name = userPlayer.getLoadedFile().getName();
            remainedTime = userPlayer.getRemainedTime();

            switch (userPlayer.getRepeat()) {
                case NO_REPEAT -> repeat = "No Repeat";
                case REPEAT_ONCE -> repeat = "Repeat Once";
                case REPEAT_INF -> repeat = "Repeat Infinite";
                case REPEAT_ALL -> repeat = "Repeat All";
                case REPEAT_CURR -> repeat = "Repeat Current Song";
            }

            shuffle = userPlayer.isShuffle();
            paused = userPlayer.getState() != Player.PlayerStatus.PLAYING;
        }
    }
}
