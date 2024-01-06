package app.statistics;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class HostStatistics extends StatisticsTemplate {
    private Map<String, Integer> topEpisodes;
    private int listeners;
}
