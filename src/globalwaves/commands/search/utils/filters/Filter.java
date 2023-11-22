package globalwaves.commands.search.utils.filters;

public interface Filter <T> {
    boolean  matches  (T MatchingObject);
}
