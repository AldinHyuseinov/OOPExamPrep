package christmasRaces.entities.races;

import christmasRaces.common.ExceptionMessages;
import christmasRaces.entities.drivers.Driver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RaceImpl implements Race {
    private String name;
    private int laps;
    private Collection<Driver> drivers;
    private static final int NAME_LENGTH = 5;

    public RaceImpl(String name, int laps) {
        setName(name);
        setLaps(laps);
        drivers = new ArrayList<>();
    }

    private void setName(String name) {

        if (name == null || name.trim().isEmpty() || name.length() < NAME_LENGTH) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.INVALID_NAME, name, NAME_LENGTH));
        }
        this.name = name;
    }

    private void setLaps(int laps) {

        if (laps < 1) {
            throw new IllegalArgumentException("Laps cannot be less than 1");
        }
        this.laps = laps;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getLaps() {
        return laps;
    }

    @Override
    public Collection<Driver> getDrivers() {
        return Collections.unmodifiableCollection(drivers);
    }

    @Override
    public void addDriver(Driver driver) {

        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null.");
        }

        if (!driver.getCanParticipate()) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.DRIVER_NOT_PARTICIPATE, driver.getName()));
        }

        if (drivers.contains(driver)) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.DRIVER_ALREADY_ADDED, driver.getName(), name));
        }

        drivers.add(driver);
    }
}
