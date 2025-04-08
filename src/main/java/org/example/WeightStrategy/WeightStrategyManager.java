package org.example.WeightStrategy;

public class WeightStrategyManager {
    private WeightStrategy weight_turu;

    public void setWeight_turu(String weight_turu) {
        this.weight_turu = WeightStrategies.fromString(weight_turu);
    }

    public WeightStrategy getWeight_turu() {
        return weight_turu;
    }
}
