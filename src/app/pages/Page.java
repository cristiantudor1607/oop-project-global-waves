package app.pages;

import app.properties.Visitor;
import app.properties.Visitable;

public abstract class Page implements Visitable {
    @Override
    public abstract String accept(Visitor v);
    public String getArtistName() {
        return null;
    }
    public String getHostName() {
        return null;
    }
    public String getUsername() {
        return null;
    }
}
