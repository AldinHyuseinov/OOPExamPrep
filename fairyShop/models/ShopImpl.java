package fairyShop.models;

import java.util.List;
import java.util.stream.Collectors;

public class ShopImpl implements Shop {
    private int brokenInstruments;

    public ShopImpl() {
        brokenInstruments = 0;
    }

    public int getBrokenInstruments() {
        return brokenInstruments;
    }

    @Override
    public void craft(Present present, Helper helper) {
        List<Instrument> healthyInstruments = helper.getInstruments()
                .stream().filter(instrument -> !instrument.isBroken()).collect(Collectors.toList());
        brokenInstruments = (int) helper.getInstruments().stream().filter(Instrument::isBroken).count();

        if (helper.canWork() && !healthyInstruments.isEmpty()) {
            Instrument currentInstrument = healthyInstruments.get(0);

            while (!present.isDone() && helper.canWork() && currentInstrument != null) {
                helper.work();
                currentInstrument.use();
                present.getCrafted();

                if (currentInstrument.isBroken()) {
                    healthyInstruments.remove(0);
                    brokenInstruments++;

                    if (!healthyInstruments.isEmpty()) {
                        currentInstrument = healthyInstruments.get(0);
                    } else {
                        currentInstrument = null;
                    }
                }
            }
        }
    }
}
