package app.search.filters;

import app.pages.Page;

public class HostPageFilter implements Filter<Page> {
    private final String hostPrefix;

    public HostPageFilter(final String hostPrefix) {
        this.hostPrefix = hostPrefix;
    }

    @Override
    public boolean matches(Page matchingObject) {
        String hostName = matchingObject.getHostName();

        if (hostName == null)
            return false;

        return hostName.startsWith(hostPrefix);
    }
}
