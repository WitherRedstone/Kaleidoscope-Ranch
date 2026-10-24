package com.chinaex123.kaleidoscope_ranch.data.animal.milk;

public class AnimalMilk {

    private int milkAmount;
    private int maxCapacity;

    public AnimalMilk() {
        this(0, 0);
    }

    public AnimalMilk(int milkAmount, int maxCapacity) {
        this.milkAmount = milkAmount;
        this.maxCapacity = maxCapacity;
    }

    public int getMilkAmount() {
        return milkAmount;
    }

    public void setMilkAmount(int amount) {
        this.milkAmount = Math.max(0, amount);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void addMilk(int amount) {
        int canAdd = maxCapacity - milkAmount;
        int actualAdd = Math.min(amount, canAdd);
        if (actualAdd > 0) {
            milkAmount += actualAdd;
        }
    }

    public int drainMilk(int amount) {
        int drained = Math.min(milkAmount, amount);
        milkAmount -= drained;
        return drained;
    }
}