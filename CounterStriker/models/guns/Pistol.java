package CounterStriker.models.guns;

public class Pistol extends GunImpl {

    public Pistol(String name, int bulletsCount) {
        super(name, bulletsCount);
    }

    @Override
    public int fire() {

        try {
            setBulletsCount(getBulletsCount() - 1);
            return 1;
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
}
