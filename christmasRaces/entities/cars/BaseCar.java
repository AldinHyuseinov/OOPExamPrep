package christmasRaces.entities.cars;

import christmasRaces.common.ExceptionMessages;

public abstract class BaseCar implements Car {
    private String model;
    private int horsePower;
    private double cubicCentimeters;
    private static final int MODEL_NAME_LENGTH = 4;

    public BaseCar(String model, int horsePower, double cubicCentimeters) {
        setModel(model);
        this.horsePower = setHorsePower(horsePower);
        this.cubicCentimeters = cubicCentimeters;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public double getCubicCentimeters() {
        return cubicCentimeters;
    }

    @Override
    public int getHorsePower() {
        return horsePower;
    }

    private void setModel(String model) {

        if (model == null || model.trim().isEmpty() || model.length() < MODEL_NAME_LENGTH) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.INVALID_MODEL, model, MODEL_NAME_LENGTH));
        }
        this.model = model;
    }

    public abstract int setHorsePower(int horsePower);

    @Override
    public double calculateRacePoints(int laps) {
        return cubicCentimeters / horsePower * laps;
    }
}
