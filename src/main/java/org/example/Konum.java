package org.example;

public class Konum {
    double baslangicEnlem;
    double baslangicBoylam;
    double hedefEnlem;
    double hedefBoylam;

    public Konum(double baslangicEnlem,double baslangicBoylam,double hedefEnlem,double hedefBoylam) {
        this.baslangicEnlem = baslangicEnlem;
        this.baslangicBoylam = baslangicBoylam;
        this.hedefEnlem = hedefEnlem;
        this.hedefBoylam = hedefBoylam;
    }

    public double getBaslangicEnlem() {
        return baslangicEnlem;
    }
    public double getBaslangicBoylam() {
        return baslangicBoylam;
    }
    public double getHedefEnlem() {
        return hedefEnlem;
    }
    public double getHedefBoylam() {
        return hedefBoylam;
    }
}
