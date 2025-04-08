package org.example.Passenger;

public class Ogrenci extends Yolcu{
    public Ogrenci(){
        super();
    }
    @Override
    public double get_indirim_orani(){
        return 0.5;
    }
}
