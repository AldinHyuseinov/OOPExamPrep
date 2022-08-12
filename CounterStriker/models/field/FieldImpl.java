package CounterStriker.models.field;

import CounterStriker.common.OutputMessages;
import CounterStriker.models.players.CounterTerrorist;
import CounterStriker.models.players.Player;
import CounterStriker.models.players.Terrorist;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FieldImpl implements Field {

    @Override
    public String start(Collection<Player> players) {
        List<Player> terrorists = players.stream().filter(player -> player instanceof Terrorist).collect(Collectors.toList());
        List<Player> counterTerrorists = players.stream().filter(player -> player instanceof CounterTerrorist).collect(Collectors.toList());

        while (!terrorists.isEmpty() && !counterTerrorists.isEmpty()) {

            for (Player terrorist : terrorists) {
                counterTerrorists.forEach(counterTerrorist -> counterTerrorist.takeDamage(terrorist.getGun().fire()));
                counterTerrorists.removeIf(counterTerrorist -> !counterTerrorist.isAlive());
            }

            for (Player counterTerrorist : counterTerrorists) {
                terrorists.forEach(terrorist -> terrorist.takeDamage(counterTerrorist.getGun().fire()));
                terrorists.removeIf(terrorist -> !terrorist.isAlive());
            }
        }

        if (terrorists.isEmpty()) {
            return OutputMessages.COUNTER_TERRORIST_WINS;
        }
        return OutputMessages.TERRORIST_WINS;
    }
}
