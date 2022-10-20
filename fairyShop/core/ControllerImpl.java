package fairyShop.core;

import fairyShop.common.ConstantMessages;
import fairyShop.common.ExceptionMessages;
import fairyShop.models.*;
import fairyShop.repositories.HelperRepository;
import fairyShop.repositories.PresentRepository;
import fairyShop.repositories.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller {
    private Repository<Helper> helperRepository;
    private Repository<Present> presentRepository;

    public ControllerImpl() {
        helperRepository = new HelperRepository();
        presentRepository = new PresentRepository();
    }

    @Override
    public String addHelper(String type, String helperName) {

        if (!type.equals("Happy") && !type.equals("Sleepy")) {
            throw new IllegalArgumentException(ExceptionMessages.HELPER_TYPE_DOESNT_EXIST);
        }

        helperRepository.add(type.equals("Happy") ? new Happy(helperName) : new Sleepy(helperName));
        return String.format(ConstantMessages.ADDED_HELPER, type, helperName);
    }

    @Override
    public String addInstrumentToHelper(String helperName, int power) {
        Helper helper = helperRepository.findByName(helperName);

        if (helper == null) {
            throw new IllegalArgumentException(ExceptionMessages.HELPER_DOESNT_EXIST);
        }

        helper.addInstrument(new InstrumentImpl(power));
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_INSTRUMENT_TO_HELPER, power, helperName);
    }

    @Override
    public String addPresent(String presentName, int energyRequired) {
        presentRepository.add(new PresentImpl(presentName, energyRequired));
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_PRESENT, presentName);
    }

    @Override
    public String craftPresent(String presentName) {
        List<Helper> readyHelpers = helperRepository.getModels().stream()
                .filter(helper -> helper.getEnergy() > 50).collect(Collectors.toList());

        if (readyHelpers.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.NO_HELPER_READY);
        }

        Present present = presentRepository.findByName(presentName);
        ShopImpl shop = new ShopImpl();

        for (Helper helper : readyHelpers) {
            shop.craft(present, helper);
        }

        StringBuilder sb = new StringBuilder();

        if (present.isDone()) {
            sb.append(String.format(ConstantMessages.PRESENT_DONE, presentName, "done"));
        } else {
            sb.append(String.format(ConstantMessages.PRESENT_DONE, presentName, "not done"));
        }

        sb.append(String.format(ConstantMessages.COUNT_BROKEN_INSTRUMENTS, shop.getBrokenInstruments()));
        return sb.toString();
    }

    @Override
    public String report() {
        StringBuilder sb = new StringBuilder((int) presentRepository.getModels().stream()
                .filter(Present::isDone).count() + " presents are done!\n");
        sb.append("Helpers info:\n");
        helperRepository.getModels().forEach(helper -> sb.append("Name: ").append(helper.getName()).append("\n")
                .append("Energy: ").append(helper.getEnergy()).append("\n").append("Instruments: ")
                .append(helper.getInstruments().stream().filter(instrument -> !instrument.isBroken()).count())
                .append(" not broken left").append("\n"));
        return sb.toString();
    }
}
