package com.chinaex123.kaleidoscope_ranch.data.animal.milk;

public class AnimalMilk {

    private double milkAmount;
    private double maxCapacity;

    public AnimalMilk() {
        this(0, 0);
    }

    public AnimalMilk(double milkAmount, double maxCapacity) {
        this.milkAmount = milkAmount;
        this.maxCapacity = maxCapacity;
    }

    public double getMilkAmount() {
        return milkAmount;
    }

    public void setMilkAmount(double amount) {
        this.milkAmount = Math.max(0, amount);
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void addMilk(double amount) {
        double canAdd = maxCapacity - milkAmount;
        double actualAdd = Math.min(amount, canAdd);
        if (actualAdd > 0) {
            milkAmount += actualAdd;
        }
    }

    public double drainMilk(double amount) {
        double drained = Math.min(milkAmount, amount);
        milkAmount -= drained;
        return drained;
    }
}