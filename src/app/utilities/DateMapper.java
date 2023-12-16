package app.utilities;

import lombok.NonNull;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateMapper {
    public static final String datePattern = "dd-MM-yyyy";
    public static final int februaryMAX = 28;
    public static final int MIN_YEAR = 1900;
    public static final int MAX_YEAR = 2023;

    private DateMapper() { }

    private static boolean dateInRange(@NonNull final LocalDate date,
                               final int minYear, final int maxYear) {

        int year = date.getYear();
        return year >= minYear && year <= maxYear;
    }

    private static boolean februaryValidDate(@NonNull final LocalDate date,
                                             final String dateAsString) {
        String[] array = dateAsString.split("-");
        if (array[1].equals("02")) {
            return Integer.parseInt(array[0]) <= februaryMAX;
        }

        return true;
    }

    public static LocalDate parseStringToDate(final String dateAsString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            TemporalAccessor tempDate = formatter.parse(dateAsString);
            LocalDate returnDate = LocalDate.from(tempDate);

            if (!dateInRange(returnDate, MIN_YEAR, MAX_YEAR))
                return null;

            if (!februaryValidDate(returnDate, dateAsString))
                return null;

            return returnDate;
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static String parseDateToString(@NonNull final LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        return formatter.format(date);
    }
}
