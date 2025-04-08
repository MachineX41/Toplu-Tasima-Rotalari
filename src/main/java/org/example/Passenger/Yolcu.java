package org.example.Passenger;

public abstract class Yolcu{
    private String name;
    private int kullanim_sayisi;

    public Yolcu(){
        this.kullanim_sayisi = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getKullanim_sayisi() {
        return kullanim_sayisi;
    }

    public void setKullanim_sayisi(int kullanim_sayisi) {
        this.kullanim_sayisi = kullanim_sayisi;
    }

    public abstract double get_indirim_orani();

}
