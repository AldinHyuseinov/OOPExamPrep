package aquarium.entities;

import aquarium.entities.aquariums.BaseAquarium;

public class FreshwaterAquarium extends BaseAquarium {
    private static final int CAPACITY = 50;

    public FreshwaterAquarium(String name) {
        super(name, CAPACITY);
    }
}
