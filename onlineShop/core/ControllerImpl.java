package onlineShop.core;

import onlineShop.common.constants.ExceptionMessages;
import onlineShop.common.constants.OutputMessages;
import onlineShop.core.interfaces.Controller;
import onlineShop.models.products.components.*;
import onlineShop.models.products.computers.Computer;
import onlineShop.models.products.computers.DesktopComputer;
import onlineShop.models.products.computers.Laptop;
import onlineShop.models.products.peripherals.*;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerImpl implements Controller {
    private Collection<Computer> computers;
    private Collection<Component> components;
    private Collection<Peripheral> peripherals;

    public ControllerImpl() {
        computers = new ArrayList<>();
        components = new ArrayList<>();
        peripherals = new ArrayList<>();
    }

    @Override
    public String addComputer(String computerType, int id, String manufacturer, String model, double price) {

        if (computers.stream().anyMatch(c -> c.getId() == id)) {
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_COMPUTER_ID);
        }

        Computer computer;

        if (computerType.equals("DesktopComputer")) {
            computer = new DesktopComputer(id, manufacturer, model, price);
        } else if (computerType.equals("Laptop")) {
            computer = new Laptop(id, manufacturer, model, price);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_COMPUTER_TYPE);
        }

        computers.add(computer);
        return String.format(OutputMessages.ADDED_COMPUTER, id);
    }

    @Override
    public String addPeripheral(int computerId, int id, String peripheralType, String manufacturer, String model, double price, double overallPerformance, String connectionType) {

        if (peripherals.stream().anyMatch(peripheral -> peripheral.getId() == id)) {
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_PERIPHERAL_ID);
        }

        Peripheral peripheral;

        switch (peripheralType) {
            case "Headset":
                peripheral = new Headset(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Keyboard":
                peripheral = new Keyboard(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Monitor":
                peripheral = new Monitor(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Mouse":
                peripheral = new Mouse(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_PERIPHERAL_TYPE);
        }

        getComputer(computerId).addPeripheral(peripheral);
        peripherals.add(peripheral);
        return String.format(OutputMessages.ADDED_PERIPHERAL, peripheralType, id, computerId);
    }

    @Override
    public String removePeripheral(String peripheralType, int computerId) {
        return String.format(OutputMessages.REMOVED_PERIPHERAL, peripheralType,
                getComputer(computerId).removePeripheral(peripheralType).getId());
    }

    @Override
    public String addComponent(int computerId, int id, String componentType, String manufacturer, String model, double price, double overallPerformance, int generation) {

        if (components.stream().anyMatch(component -> component.getId() == id)) {
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_COMPONENT_ID);
        }

        Component component;

        switch (componentType) {
            case "CentralProcessingUnit":
                component = new CentralProcessingUnit(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "Motherboard":
                component = new Motherboard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "PowerSupply":
                component = new PowerSupply(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "RandomAccessMemory":
                component = new RandomAccessMemory(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "SolidStateDrive":
                component = new SolidStateDrive(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "VideoCard":
                component = new VideoCard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_COMPONENT_TYPE);
        }

        getComputer(computerId).addComponent(component);
        components.add(component);
        return String.format(OutputMessages.ADDED_COMPONENT, componentType, id, computerId);
    }

    @Override
    public String removeComponent(String componentType, int computerId) {
        return String.format(OutputMessages.REMOVED_COMPONENT, componentType,
                getComputer(computerId).removeComponent(componentType).getId());
    }

    @Override
    public String buyComputer(int id) {
        Computer computer = getComputer(id);
        computers.remove(computer);
        return computer.toString();
    }

    @Override
    public String BuyBestComputer(double budget) {
        double bestPerformance = Double.MIN_VALUE;
        Computer pc = null;

        for (Computer computer : computers) {
            double performance = computer.getOverallPerformance();

            if (performance > bestPerformance && computer.getPrice() <= budget) {
                pc = computer;
                bestPerformance = performance;
            }
        }

        if (pc == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.CAN_NOT_BUY_COMPUTER, budget));
        }

        computers.remove(pc);
        return pc.toString();
    }

    @Override
    public String getComputerData(int id) {
        return getComputer(id).toString();
    }

    private Computer getComputer(int computerId) {
        Computer computer = computers.stream().filter(c -> c.getId() == computerId).findFirst().orElse(null);

        if (computer == null) {
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        return computer;
    }
}
