package app.utilities;

import lombok.NonNull;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public final class DateMapper {
    public static final String DD_MM_YYYY = "dd-MM-yyyy";
    public static final int FEBRUARY = 28;
    public static final int MIN_YEAR = 1900;
    public static final int MAX_YEAR = 2023;

    private DateMapper() { }

    /**
     * Checks if the date's year falls within {@code minYear} and {@code maxYear}.
     * @param date The date to be checked
     * @param minYear The minimum year accepted
     * @param maxYear The maximum year accepted
     * @return true, if the date falls within, false, otherwise
     */
    private static boolean dateInRange(@NonNull final LocalDate date,
                               final int minYear, final int maxYear) {

        int year = date.getYear();
        return year >= minYear && year <= maxYear;
    }

    /**
     * Checks if the date in {@code DD_MM_YYYY} format is a valid february date.
     * It doesn't check the leap years, yet.
     * @param dateAsString The date in {@code DD_MM_YYYY} format
     * @return true, if the date is valid, false, otherwise
     */
    private static boolean februaryValidDate(final String dateAsString) {
        String[] array = dateAsString.split("-");
        if (array[1].equals("02")) {
            return Integer.parseInt(array[0]) <= FEBRUARY;
        }

        return true;
    }

    /**
     * Converts the String that contains a date in the {@code DD_MM_YYYY} format into
     * a {@code LocalDate}.
     * @param dateAsString The date to be converted
     * @return The date as {@code LocalDate}, if the string contains a valid date,
     * null otherwise
     */
    public static LocalDate parseStringToDate(final String dateAsString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MM_YYYY);
            TemporalAccessor tempDate = formatter.parse(dateAsString);
            LocalDate returnDate = LocalDate.from(tempDate);

            if (!dateInRange(returnDate, MIN_YEAR, MAX_YEAR)) {
                return null;
            }

            if (!februaryValidDate(dateAsString)) {
                return null;
            }

            return returnDate;
        } catch (DateTimeException e) {
            return null;
        }
    }

    /**
     * Converts a {@code LocalDate} date into a String
     * @param date The date to be converted
     * @return A string containing the date in {@code DD_MM_YYYY} format
     */
    public static String parseDateToString(@NonNull final LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MM_YYYY);

        return formatter.format(date);
    }
}
