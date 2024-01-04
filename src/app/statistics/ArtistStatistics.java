package app.statistics;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ArtistStatistics extends StatisticsTemplate {
    private Map<String, Integer> topAlbums;
    private Map<String, Integer> topSongs;
    private List<String> topFans;
    private int listeners;
}
