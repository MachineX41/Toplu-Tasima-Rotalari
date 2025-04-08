package org.example.Rota;


public class WalkingController extends Vehicle {
    @Override
    public String get_name(){
        return "walking";
    }

    @Override
    double get_sure(double distance) {
        return distance*10;
    }

    @Override
    double get_ucret(double distance) {
        return 0.0;
    }
}
