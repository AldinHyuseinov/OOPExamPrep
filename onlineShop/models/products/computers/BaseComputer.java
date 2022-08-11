package onlineShop.models.products.computers;

import onlineShop.common.constants.ExceptionMessages;
import onlineShop.models.products.BaseProduct;
import onlineShop.models.products.Product;
import onlineShop.models.products.components.Component;
import onlineShop.models.products.peripherals.Peripheral;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseComputer extends BaseProduct implements Computer {
    private List<Component> components;
    private List<Peripheral> peripherals;

    protected BaseComputer(int id, String manufacturer, String model, double price, double overallPerformance) {
        super(id, manufacturer, model, price, overallPerformance);
        components = new ArrayList<>();
        peripherals = new ArrayList<>();
    }

    @Override
    public List<Component> getComponents() {
        return components;
    }

    @Override
    public List<Peripheral> getPeripherals() {
        return peripherals;
    }

    @Override
    public void addComponent(Component component) {

        if (components.contains(component)) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.EXISTING_COMPONENT,
                    component.getClass().getSimpleName(), this.getClass().getSimpleName(), getId()));
        }
        components.add(component);
    }

    @Override
    public Component removeComponent(String componentType) {
        Component component = components.stream().filter(c ->
                c.getClass().getSimpleName().equals(componentType)).findFirst().orElse(null);

        if (components.isEmpty() || component == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NOT_EXISTING_COMPONENT,
                    componentType, this.getClass().getSimpleName(), getId()));
        }

        components.remove(component);
        return component;
    }

    @Override
    public void addPeripheral(Peripheral peripheral) {

        if (peripherals.contains(peripheral)) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.EXISTING_PERIPHERAL,
                    peripheral.getClass().getSimpleName(), this.getClass().getSimpleName(), getId()));
        }
        peripherals.add(peripheral);
    }

    @Override
    public Peripheral removePeripheral(String peripheralType) {
        Peripheral peripheral = peripherals.stream().filter(p -> p.getClass().getSimpleName().equals(peripheralType))
                .findFirst().orElse(null);

        if (peripherals.isEmpty() || peripheral == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NOT_EXISTING_PERIPHERAL, peripheralType, this.getClass().getSimpleName(), getId()));
        }

        peripherals.remove(peripheral);
        return peripheral;
    }

    @Override
    public double getOverallPerformance() {
        return components.isEmpty() ? super.getOverallPerformance() : super.getOverallPerformance() + components.stream().mapToDouble(Product::getOverallPerformance).average().getAsDouble();
    }

    @Override
    public double getPrice() {
        return super.getPrice() + components.stream().mapToDouble(Product::getPrice).sum() + peripherals.stream().mapToDouble(Product::getPrice).sum();
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(super.toString()).append("\n").append(" Components (").append(components.size()).append("):").append("\n");
        components.forEach(component -> output.append("  ").append(component.toString()).append("\n"));
        output.append(" Peripherals (").append(peripherals.size()).append("); ").append("Average Overall Performance (")
                .append(String.format("%.2f", peripherals.stream().mapToDouble(Product::getOverallPerformance).average().orElse(0.00))).append("):").append("\n");
        peripherals.forEach(peripheral -> output.append("  ").append(peripheral.toString()).append("\n"));
        return output.toString().trim();
    }
}
