package org.example.Passenger;

public class PassengerManager{
    private Yolcu yolcu;

    public void setYolcu(String yolcu_turu){
        yolcu = PassengerType.fromString(yolcu_turu); //String yolcu turunu karşılık gelen nesneye çeviriyor
    }

    public Yolcu get_yolcu(){
        return yolcu;
    }

}   