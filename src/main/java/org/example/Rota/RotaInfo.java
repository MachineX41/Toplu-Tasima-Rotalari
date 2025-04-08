package org.example.Rota;

import org.example.DijkstraAlghorithm.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;

public class RotaInfo{
    private String start_type;
    private String end_type;
    private ArrayList<Coordinate> coordinates;
    private Double toplam_mesafe;
    private Double toplam_sure;
    private Double toplam_ucret;
    private HashMap<String,Object> path_info;

    public RotaInfo(){
        path_info = new HashMap<>();
        coordinates = new ArrayList<>();
        toplam_mesafe = 0.0;
        toplam_sure = 0.0;
        toplam_ucret = 0.0;
    }

    public void setStart_type(String start_type) {
        this.start_type = start_type;
    }

    public void setEnd_type(String end_type) {
        this.end_type = end_type;
    }

    public ArrayList<Coordinate> getCoordinates(){return this.coordinates;}

    public void setToplam_mesafe(Double toplam_mesafe) {
        this.toplam_mesafe = toplam_mesafe;
    }

    public void setToplam_sure(Double toplam_sure) {
        this.toplam_sure = toplam_sure;
    }

    public void setToplam_ucret(Double toplam_ucret) {
        this.toplam_ucret = toplam_ucret;
    }

    public Double getToplam_mesafe(){ return this.toplam_mesafe;}

    public Double getToplam_ucret(){ return this.toplam_ucret;}

    public Double getToplam_sure(){ return this.toplam_sure;}

    public void addPath_info() {
        path_info.put("start_type", this.start_type);
        path_info.put("end_type", this.end_type);
        path_info.put("coordinates", this.coordinates);
        path_info.put("toplam_mesafe_km", this.toplam_mesafe);
        path_info.put("toplam_sure_dk", this.toplam_sure);
        path_info.put("toplam_ucret", this.toplam_ucret);
    }

    public HashMap<String,Object> get_path_info() {
        return path_info;
    }


}
