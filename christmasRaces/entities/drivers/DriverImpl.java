package christmasRaces.entities.drivers;

import christmasRaces.common.ExceptionMessages;
import christmasRaces.entities.cars.Car;

public class DriverImpl implements Driver {
    private String name;
    private Car car;
    private int numberOfWins;
    private boolean canParticipate;
    private static final int NAME_LENGTH = 5;

    public DriverImpl(String name) {
        setName(name);
    }

    private void setName(String name) {

        if (name == null || name.trim().isEmpty() || name.length() < NAME_LENGTH) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.INVALID_NAME, name, NAME_LENGTH));
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Car getCar() {
        return car;
    }

    @Override
    public int getNumberOfWins() {
        return numberOfWins;
    }

    @Override
    public void addCar(Car car) {

        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null.");
        }
        this.car = car;
        canParticipate = true;
    }

    @Override
    public void winRace() {
        numberOfWins++;
    }

    @Override
    public boolean getCanParticipate() {
        return canParticipate;
    }
}
