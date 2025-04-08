package org.example.Payment;

public class PaymentManager {
    private Odeme odeme_turu;

    public void setOdeme_turu(String odeme_turu) {
        this.odeme_turu = PaymentType.fromString(odeme_turu);
    }

    public Odeme getOdeme_turu() {
        return odeme_turu;
    }

}
