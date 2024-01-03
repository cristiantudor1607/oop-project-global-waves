package app.search.filters;

import app.pages.Page;

public class HostPageFilter implements Filter<Page> {
    private final String hostPrefix;

    public HostPageFilter(final String hostPrefix) {
        this.hostPrefix = hostPrefix;
    }

    /**
     * Checks if the host page name starts with {@code hostPrefix}.
     * @param matchingObject The object to be matched
     * @return true, if it matches, false, otherwise
     */
    @Override
    public boolean matches(final Page matchingObject) {
        String hostName = matchingObject.getHostName();

        return hostName != null && hostName.startsWith(hostPrefix);

    }
}
