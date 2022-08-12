package CounterStriker.repositories;

import CounterStriker.common.ExceptionMessages;
import CounterStriker.models.players.Player;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerRepository implements Repository<Player> {
    private Collection<Player> models;

    public PlayerRepository() {
        models = new ArrayList<>();
    }

    @Override
    public Collection<Player> getModels() {
        return models;
    }

    @Override
    public void add(Player model) {

        if (model == null) {
            throw new NullPointerException(ExceptionMessages.INVALID_PLAYER_REPOSITORY);
        }
        models.add(model);
    }

    @Override
    public boolean remove(Player model) {
        return models.remove(model);
    }

    @Override
    public Player findByName(String name) {
        return models.stream().filter(player -> player.getUsername().equals(name)).findFirst().orElse(null);
    }
}
