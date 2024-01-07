package app.utilities;

import app.player.entities.monetization.MonetizationStat;

import java.util.Comparator;
import java.util.Map;

public class SortByRevenue<T> implements Comparator<Map.Entry<T, MonetizationStat>> {
    // TODO: Add doc
    /**
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(Map.Entry<T, MonetizationStat> o1, Map.Entry<T, MonetizationStat> o2) {
        Double o1TotalRevenue = o1.getValue().getMerchRevenue()
                + o1.getValue().getSongRevenue();

        Double o2TotalRevenue = o2.getValue().getMerchRevenue()
                + o2.getValue().getSongRevenue();

        return o1TotalRevenue.compareTo(o2TotalRevenue);
    }
}
