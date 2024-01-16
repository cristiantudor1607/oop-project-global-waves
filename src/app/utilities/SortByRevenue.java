package app.utilities;

import app.monetization.MonetizationSummary;

import java.util.Comparator;
import java.util.Map;

public class SortByRevenue<T> implements Comparator<Map.Entry<T, MonetizationSummary>> {
    /**
     * Compares the revenues from the values of the two map entries.
     * @param o1 the first entry to be compared.
     * @param o2 the second entry to be compared.
     * @return 0 if o2 revenue is numerically equal to o1 revenue;
     * a value less than 0 if o1 revenue is numerically less than o2 revenue;
     * and a value greater than 0 if o1 revenue is numerically greater than o2 revenue.
     */
    @Override
    public int compare(final Map.Entry<T, MonetizationSummary> o1,
                       final Map.Entry<T, MonetizationSummary> o2) {
        Double o1TotalRevenue = o1.getValue().getMerchRevenue()
                + o1.getValue().getSongRevenue();

        Double o2TotalRevenue = o2.getValue().getMerchRevenue()
                + o2.getValue().getSongRevenue();

        return o1TotalRevenue.compareTo(o2TotalRevenue);
    }
}
