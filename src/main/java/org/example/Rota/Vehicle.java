package org.example.Rota;

import org.example.DijkstraAlghorithm.Coordinate;

import java.util.List;
import java.util.Map;

public abstract class Vehicle {
    public void create_way(RotaInfo path_info, Coordinate coordinate, double distance , String start_or_end) {
        if(start_or_end.equals("start_type")){
            path_info.setStart_type(this.get_name());
        }
        else if(start_or_end.equals("end_type")){
            path_info.setEnd_type(this.get_name());
        }

        path_info.getCoordinates().add(coordinate);


        path_info.setToplam_mesafe(path_info.getToplam_mesafe() + distance);
        path_info.setToplam_ucret(path_info.getToplam_ucret() + this.get_ucret(distance));
        path_info.setToplam_sure(path_info.getToplam_sure() + this.get_sure(distance));

        path_info.addPath_info();
    }

    abstract String get_name();
    abstract double get_sure(double distance);
    abstract double get_ucret(double distance);
}
