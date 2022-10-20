package catHouse.core;

import catHouse.common.ConstantMessages;
import catHouse.common.ExceptionMessages;
import catHouse.entities.cat.Cat;
import catHouse.entities.cat.LonghairCat;
import catHouse.entities.cat.ShorthairCat;
import catHouse.entities.houses.House;
import catHouse.entities.houses.LongHouse;
import catHouse.entities.houses.ShortHouse;
import catHouse.entities.toys.Ball;
import catHouse.entities.toys.Mouse;
import catHouse.entities.toys.Toy;
import catHouse.repositories.ToyRepository;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerImpl implements Controller {
    private ToyRepository toys;
    private Collection<House> houses;

    public ControllerImpl() {
        toys = new ToyRepository();
        houses = new ArrayList<>();
    }

    @Override
    public String addHouse(String type, String name) {
        House house;

        if (type.equals("ShortHouse")) {
            house = new ShortHouse(name);
        } else if (type.equals("LongHouse")) {
            house = new LongHouse(name);
        } else {
            throw new NullPointerException(ExceptionMessages.INVALID_HOUSE_TYPE);
        }

        houses.add(house);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_HOUSE_TYPE, type);
    }

    @Override
    public String buyToy(String type) {
        Toy toy;

        if (type.equals("Ball")) {
            toy = new Ball();
        } else if (type.equals("Mouse")) {
            toy = new Mouse();
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_TOY_TYPE);
        }

        toys.buyToy(toy);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_TOY_TYPE, type);
    }

    @Override
    public String toyForHouse(String houseName, String toyType) {
        Toy toy = toys.findFirst(toyType);

        if (toy == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_TOY_FOUND, toyType));
        }

        houses.stream().filter(house -> house.getName().equals(houseName)).findFirst().get().buyToy(toy);
        toys.removeToy(toy);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_TOY_IN_HOUSE, toyType, houseName);
    }

    @Override
    public String addCat(String houseName, String catType, String catName, String catBreed, double price) {
        Cat cat;

        if (catType.equals("ShorthairCat")) {
            cat = new ShorthairCat(catName, catBreed, price);
        } else if (catType.equals("LonghairCat")) {
            cat = new LonghairCat(catName, catBreed, price);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_CAT_TYPE);
        }

        House house = houses.stream().filter(h -> h.getName().equals(houseName)).findFirst().get();

        if (cat instanceof ShorthairCat && house instanceof LongHouse || cat instanceof LonghairCat && house instanceof ShortHouse) {
            return ConstantMessages.UNSUITABLE_HOUSE;
        }

        house.addCat(cat);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_CAT_IN_HOUSE, catType, houseName);
    }

    @Override
    public String feedingCat(String houseName) {
        House home = houses.stream().filter(house -> house.getName().equals(houseName)).findFirst().get();

        home.feeding();
        return String.format(ConstantMessages.FEEDING_CAT, home.getCats().size());
    }

    @Override
    public String sumOfAll(String houseName) {
        House home = houses.stream().filter(house -> house.getName().equals(houseName)).findFirst().get();

        return String.format(ConstantMessages.VALUE_HOUSE, houseName, home.getCats().stream().mapToDouble(Cat::getPrice).sum() +
                home.getToys().stream().mapToDouble(Toy::getPrice).sum());
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        houses.forEach(house -> sb.append(house.getStatistics()));
        return sb.toString();
    }
}
