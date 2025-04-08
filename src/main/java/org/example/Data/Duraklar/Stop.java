package org.example.Data.Duraklar;

import org.example.DijkstraAlghorithm.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Stop {
    private String id;
    private String type;
    private Coordinate coordinate;
    private ArrayList<Stop> nextStops;
    private HashMap<Stop,Double> mesafe;
    private HashMap<Stop,Double> sure;
    private HashMap<Stop,Double> ucret;

    Stop(){
       nextStops = new ArrayList<>();
       mesafe = new HashMap<>();
       sure = new HashMap<>();
       ucret = new HashMap<>();
    }

    // Getter metotları
    public abstract String get_name();

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ArrayList<Stop> getNextStops() {
        return nextStops;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void addNextStop(Stop stop){
        nextStops.add(stop);
    }

    public void add_mesafe(Stop stop,Double distance){
        mesafe.put(stop,distance);
    }

    public void add_ucret(Stop stop,Double price){
        ucret.put(stop,price);
    }

    public void add_sure(Stop stop, Double time) {
        sure.put(stop,time);
    }

    public HashMap<Stop, Double> getMesafe() {
        return mesafe;
    }

    public HashMap<Stop, Double> getSure() {
        return sure;
    }

    public HashMap<Stop, Double> getUcret() {
        return ucret;
    }


}
