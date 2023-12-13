package globalwaves.player.entities;

import lombok.Getter;

@Getter
public class Merch {
    private final String name;
    private final String description;
    private final int price;

    public Merch(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
