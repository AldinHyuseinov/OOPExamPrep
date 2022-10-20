package aquarium.entities.fish;

import aquarium.entities.aquariums.BaseAquarium;

public class SaltwaterAquarium extends BaseAquarium {
    private static final int CAPACITY = 25;

    public SaltwaterAquarium(String name) {
        super(name, CAPACITY);
    }
}
