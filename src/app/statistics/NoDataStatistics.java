package app.statistics;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoDataStatistics extends StatisticsTemplate{
    private String message;

    /**
     * If there are no statistics, it returns the user-specific "No data to show" message.
     * @return {@code null}, if the user has statistics, the "No data to show" message,
     * otherwise
     */
    @Override
    public String getSpecificMessage() {
        return message;
    }
}
