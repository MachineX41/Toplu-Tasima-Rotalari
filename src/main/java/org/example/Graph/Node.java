package org.example.Graph;

import org.example.DijkstraAlghorithm.Coordinate;

import java.util.ArrayList;

public class Node {
    private ArrayList<Edge> edges;
    private final String name;
    private double lat;
    private double lon;

    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<Edge>();
    }

    public void setCoordinates(double lat,double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public Coordinate getCoordinates(){
        return new Coordinate(String.valueOf(this.lat),String.valueOf(this.lon));
    }

    public void add_edge(Node end, double mesafe, double sure, double ucret, String baglanti_tipi){
        this.edges.add(new Edge(this,end,mesafe,sure,ucret,baglanti_tipi));
    }

    public String get_name(){
        return this.name;
    }

    public ArrayList<Edge> get_edges() {
        return this.edges;
    }

}