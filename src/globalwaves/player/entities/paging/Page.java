package globalwaves.player.entities.paging;

import globalwaves.player.entities.properties.Visitor;
import globalwaves.player.entities.properties.Visitable;

public abstract class Page implements Visitable {
    @Override
    public abstract String accept(Visitor v);
}
