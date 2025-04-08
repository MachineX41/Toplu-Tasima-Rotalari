package org.example.Graph;

public class Edge {
    private final Node start;
    private final Node end;
    private double weight_strategy;
    private final double mesafe;
    private final double sure;
    private final double ucret;
    private final String baglanti_tipi;

    public Edge(Node start, Node end, double mesafe, double sure, double ucret, String baglanti_tipi) {
        this.start = start;
        this.end = end;
        this.mesafe = mesafe;
        this.sure = sure;
        this.ucret = ucret;
        this.baglanti_tipi = baglanti_tipi;
    }

    public Node getStart() {
        return start;
    }
    public Node getEnd() {
        return end;
    }

    public void setWeight_strategy(double weight_strategy){
        this.weight_strategy = weight_strategy;
    }

    public double getWeight(){ return weight_strategy;}

    public double getMesafe() { return mesafe; }
    public double getSure() { return sure; }
    public double getUcret() { return ucret; }
    public String getBaglanti_tipi() { return baglanti_tipi; }

}
