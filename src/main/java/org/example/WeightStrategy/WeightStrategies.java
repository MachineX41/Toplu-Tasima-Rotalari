package org.example.WeightStrategy;


public enum WeightStrategies {
    mesafe(new DistanceWeightStrategy()),
    sure(new TimeWeightStrategy()),
    ucret(new CostWeightStrategy());

    WeightStrategy strategy;
    WeightStrategies(WeightStrategy weightStrategy) {
        this.strategy = weightStrategy;
    }

    // String türündeki girişleri daha güvenli çevirmek için bir yardımcı metot
    public static WeightStrategy fromString(String text) {
        for (WeightStrategies pt : WeightStrategies.values()) {
            if (pt.name().equalsIgnoreCase(text)) {
                return pt.strategy;
            }
        }
        throw new IllegalArgumentException("Geçersiz ödeme türü: " + text);
    }

}
