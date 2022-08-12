package CounterStriker.core;

import CounterStriker.common.ExceptionMessages;
import CounterStriker.common.OutputMessages;
import CounterStriker.models.field.Field;
import CounterStriker.models.field.FieldImpl;
import CounterStriker.models.guns.Gun;
import CounterStriker.models.guns.Pistol;
import CounterStriker.models.guns.Rifle;
import CounterStriker.models.players.CounterTerrorist;
import CounterStriker.models.players.Player;
import CounterStriker.models.players.Terrorist;
import CounterStriker.repositories.GunRepository;
import CounterStriker.repositories.PlayerRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller {
    private GunRepository guns;
    private PlayerRepository players;
    private Field field;

    public ControllerImpl() {
        guns = new GunRepository();
        players = new PlayerRepository();
        field = new FieldImpl();
    }

    @Override
    public String addGun(String type, String name, int bulletsCount) {
        Gun gun;

        if (type.equals("Pistol")) {
            gun = new Pistol(name, bulletsCount);
        } else if (type.equals("Rifle")) {
            gun = new Rifle(name, bulletsCount);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_GUN_TYPE);
        }

        guns.add(gun);
        return String.format(OutputMessages.SUCCESSFULLY_ADDED_GUN, name);
    }

    @Override
    public String addPlayer(String type, String username, int health, int armor, String gunName) {
        Gun gun = guns.findByName(gunName);

        if (gun == null) {
            throw new NullPointerException(ExceptionMessages.GUN_CANNOT_BE_FOUND);
        }

        Player player;

        if (type.equals("Terrorist")) {
            player = new Terrorist(username, health, armor, gun);
        } else if (type.equals("CounterTerrorist")) {
            player = new CounterTerrorist(username, health, armor, gun);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_PLAYER_TYPE);
        }

        players.add(player);
        return String.format(OutputMessages.SUCCESSFULLY_ADDED_PLAYER, username);
    }

    @Override
    public String startGame() {
        return field.start(players.getModels().stream().filter(Player::isAlive).collect(Collectors.toList()));
    }

    @Override
    public String report() {
        List<Player> counterTerrorists = players.getModels().stream().filter(player -> player instanceof CounterTerrorist).collect(Collectors.toList());
        List<Player> terrorists = players.getModels().stream().filter(player -> player instanceof Terrorist).collect(Collectors.toList());

        StringBuilder output = new StringBuilder();

        counterTerrorists.stream().sorted(Comparator.comparing(Player::getHealth).reversed())
                .sorted(Comparator.comparing(Player::getUsername)).forEach(output::append);

        terrorists.stream().sorted(Comparator.comparing(Player::getHealth).reversed())
                .sorted(Comparator.comparing(Player::getUsername)).forEach(output::append);

        return output.toString().trim();
    }
}
