package christmasRaces.entities.cars;

import christmasRaces.common.ExceptionMessages;

public class SportsCar extends BaseCar {
    private static final double SPORTS_CAR_CUBIC_CENTIMETERS = 3000;
    private static final int SPORTS_CAR_MIN_HORSEPOWER = 250;
    private static final int SPORTS_CAR_MAX_HORSEPOWER = 450;

    public SportsCar(String model, int horsePower) {
        super(model, horsePower, SPORTS_CAR_CUBIC_CENTIMETERS);
    }

    @Override
    public int setHorsePower(int horsePower) {

        if (horsePower < SPORTS_CAR_MIN_HORSEPOWER || horsePower > SPORTS_CAR_MAX_HORSEPOWER) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.INVALID_HORSE_POWER, horsePower));
        }
        return horsePower;
    }
}
