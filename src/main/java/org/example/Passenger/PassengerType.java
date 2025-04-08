package org.example.Passenger;

public enum PassengerType {
    Genel,
    Ogrenci,
    Yasli;

    public Yolcu createPassenger() {
        switch (this) {
            case Genel:
                return new Genel();
            case Ogrenci:
                return new Ogrenci();
            case Yasli:
                return new Yasli();
            default:
                throw new IllegalStateException("Bilinmeyen yolcu türü");
        }
    }

    public static Yolcu fromString(String text) {
        for (PassengerType pt : PassengerType.values()) {
            if (pt.name().equalsIgnoreCase(text)) {
                return pt.createPassenger();
            }
        }
        throw new IllegalArgumentException("Geçersiz yolcu türü: " + text);
    }
}
