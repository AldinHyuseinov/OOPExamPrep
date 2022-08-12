package CounterStriker.models.guns;

public class Rifle extends GunImpl {

    public Rifle(String name, int bulletsCount) {
        super(name, bulletsCount);
    }

    @Override
    public int fire() {

        try {
            setBulletsCount(getBulletsCount() - 10);
            return 10;
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
}
