package aquarium.core;

import aquarium.common.ConstantMessages;
import aquarium.common.ExceptionMessages;
import aquarium.entities.FreshwaterAquarium;
import aquarium.entities.aquariums.Aquarium;
import aquarium.entities.decorations.Decoration;
import aquarium.entities.decorations.Ornament;
import aquarium.entities.decorations.Plant;
import aquarium.entities.fish.Fish;
import aquarium.entities.fish.FreshwaterFish;
import aquarium.entities.fish.SaltwaterAquarium;
import aquarium.entities.fish.SaltwaterFish;
import aquarium.repositories.DecorationRepository;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerImpl implements Controller {
    private DecorationRepository decorations;
    private Collection<Aquarium> aquariums;

    public ControllerImpl() {
        decorations = new DecorationRepository();
        aquariums = new ArrayList<>();
    }

    @Override
    public String addAquarium(String aquariumType, String aquariumName) {
        Aquarium aquarium;

        if (aquariumType.equals("FreshwaterAquarium")) {
            aquarium = new FreshwaterAquarium(aquariumName);
        } else if (aquariumType.equals("SaltwaterAquarium")) {
            aquarium = new SaltwaterAquarium(aquariumName);
        } else {
            throw new NullPointerException(ExceptionMessages.INVALID_AQUARIUM_TYPE);
        }

        aquariums.add(aquarium);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_AQUARIUM_TYPE, aquariumType);
    }

    @Override
    public String addDecoration(String type) {
        Decoration decoration;

        if (type.equals("Ornament")) {
            decoration = new Ornament();
        } else if (type.equals("Plant")) {
            decoration = new Plant();
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_DECORATION_TYPE);
        }

        decorations.add(decoration);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_DECORATION_TYPE, type);
    }

    @Override
    public String insertDecoration(String aquariumName, String decorationType) {
        Decoration decoration = decorations.findByType(decorationType);

        if (decoration == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_DECORATION_FOUND, decorationType));
        }

        getAquarium(aquariumName).addDecoration(decoration);
        decorations.remove(decoration);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_DECORATION_IN_AQUARIUM, decorationType, aquariumName);
    }

    @Override
    public String addFish(String aquariumName, String fishType, String fishName, String fishSpecies, double price) {
        Fish fish;

        if (fishType.equals("FreshwaterFish")) {
            fish = new FreshwaterFish(fishName, fishSpecies, price);
        } else if (fishType.equals("SaltwaterFish")) {
            fish = new SaltwaterFish(fishName, fishSpecies, price);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_FISH_TYPE);
        }

        Aquarium aquarium = getAquarium(aquariumName);

        boolean isFreshwaterFishInFreshwaterAquarium = fish instanceof FreshwaterFish && aquarium instanceof FreshwaterAquarium;
        boolean isSaltwaterFishInSaltwaterAquarium = fish instanceof SaltwaterFish && aquarium instanceof SaltwaterAquarium;

        if (isFreshwaterFishInFreshwaterAquarium || isSaltwaterFishInSaltwaterAquarium) {
            aquarium.addFish(fish);
            return String.format(ConstantMessages.SUCCESSFULLY_ADDED_FISH_IN_AQUARIUM, fishType, aquariumName);
        }
        return ConstantMessages.WATER_NOT_SUITABLE;
    }

    @Override
    public String feedFish(String aquariumName) {
        getAquarium(aquariumName).feed();
        return String.format(ConstantMessages.FISH_FED, getAquarium(aquariumName).getFish().size());
    }

    @Override
    public String calculateValue(String aquariumName) {
        Aquarium aquarium = getAquarium(aquariumName);
        return String.format(ConstantMessages.VALUE_AQUARIUM, aquariumName, aquarium.getFish().stream()
                .mapToDouble(Fish::getPrice).sum() + aquarium.getDecorations().stream().mapToDouble(Decoration::getPrice).sum());
    }

    @Override
    public String report() {
        StringBuilder output = new StringBuilder();
        aquariums.forEach(aquarium -> output.append(aquarium.getInfo()).append("\n"));
        return output.toString().trim();
    }

    private Aquarium getAquarium(String name) {
        return aquariums.stream().filter(a -> a.getName().equals(name)).findFirst().get();
    }
}
