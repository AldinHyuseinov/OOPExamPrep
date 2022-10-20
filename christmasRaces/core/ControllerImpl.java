package christmasRaces.core;

import christmasRaces.common.ExceptionMessages;
import christmasRaces.common.OutputMessages;
import christmasRaces.core.interfaces.Controller;
import christmasRaces.entities.cars.BaseCar;
import christmasRaces.entities.cars.Car;
import christmasRaces.entities.cars.MuscleCar;
import christmasRaces.entities.cars.SportsCar;
import christmasRaces.entities.drivers.Driver;
import christmasRaces.entities.drivers.DriverImpl;
import christmasRaces.entities.races.Race;
import christmasRaces.entities.races.RaceImpl;
import christmasRaces.repositories.CarRepository;
import christmasRaces.repositories.DriverRepository;
import christmasRaces.repositories.RaceRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller {
    private DriverRepository driverRepository;
    private CarRepository carRepository;
    private RaceRepository raceRepository;
    private static final int MIN_PARTICIPANTS = 3;

    public ControllerImpl(DriverRepository driverRepository, CarRepository carRepository, RaceRepository raceRepository) {
        this.driverRepository = driverRepository;
        this.carRepository = carRepository;
        this.raceRepository = raceRepository;
    }

    @Override
    public String createDriver(String driverName) {

        if (driverRepository.getByName(driverName) != null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.DRIVER_EXISTS, driverName));
        }

        driverRepository.add(new DriverImpl(driverName));
        return String.format(OutputMessages.DRIVER_CREATED, driverName);
    }

    @Override
    public String createCar(String type, String model, int horsePower) {

        if (carRepository.getByName(model) != null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.CAR_EXISTS, model));
        }

        BaseCar car;

        if (type.equals("Muscle")) {
            car = new MuscleCar(model, horsePower);
        } else {
            car = new SportsCar(model, horsePower);
        }

        carRepository.add(car);
        return String.format(OutputMessages.CAR_CREATED, type + "Car", model);
    }

    @Override
    public String addCarToDriver(String driverName, String carModel) {
        Driver driver = driverRepository.getByName(driverName);
        Car car = carRepository.getByName(carModel);

        if (driver == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.DRIVER_NOT_FOUND, driverName));
        }

        if (car == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.CAR_NOT_FOUND, carModel));
        }

        driver.addCar(car);
        return String.format(OutputMessages.CAR_ADDED, driverName, carModel);
    }

    @Override
    public String addDriverToRace(String raceName, String driverName) {
        Race race = raceRepository.getByName(raceName);
        Driver driver = driverRepository.getByName(driverName);

        if (race == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.RACE_NOT_FOUND, raceName));
        }

        if (driver == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.DRIVER_NOT_FOUND, driverName));
        }

        race.addDriver(driver);
        return String.format(OutputMessages.DRIVER_ADDED, driverName, raceName);
    }

    @Override
    public String startRace(String raceName) {
        Race race = raceRepository.getByName(raceName);

        if (race == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.RACE_NOT_FOUND, raceName));
        }

        if (driverRepository.getAll().size() < MIN_PARTICIPANTS) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.RACE_INVALID, raceName, MIN_PARTICIPANTS));
        }

        List<Driver> drivers = driverRepository.getAll().stream()
                .sorted(Comparator.comparing(driver -> driver.getCar().calculateRacePoints(race.getLaps()), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        drivers.get(0).winRace();
        return String.format(OutputMessages.DRIVER_FIRST_POSITION, drivers.get(0).getName(), raceName) + "\n" +
                String.format(OutputMessages.DRIVER_SECOND_POSITION, drivers.get(1).getName(), raceName) + "\n" +
                String.format(OutputMessages.DRIVER_THIRD_POSITION, drivers.get(2).getName(), raceName);
    }

    @Override
    public String createRace(String name, int laps) {

        if (raceRepository.getByName(name) != null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.RACE_EXISTS, name));
        }

        raceRepository.add(new RaceImpl(name, laps));
        return String.format(OutputMessages.RACE_CREATED, name);
    }
}
