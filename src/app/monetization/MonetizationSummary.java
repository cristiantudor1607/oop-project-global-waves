package app.monetization;

import app.player.entities.Song;
import app.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MonetizationSummary {
    private final Double songRevenue;
    private final Double merchRevenue;
    @Setter
    private int ranking;
    private final String mostProfitableSong;

    private static final double HUNDRED = 100.0;

    public MonetizationSummary(final User artist) {
        songRevenue = Math.round(artist.getSongRevenue() * HUNDRED) / HUNDRED;
        merchRevenue = Math.round(artist.getMerchRevenue() * HUNDRED) / HUNDRED;

        Song song = artist.getMostProfitableSong();
        if (song == null) {
            mostProfitableSong = "N/A";
        } else {
            mostProfitableSong = song.getName();
        }
    }
}
