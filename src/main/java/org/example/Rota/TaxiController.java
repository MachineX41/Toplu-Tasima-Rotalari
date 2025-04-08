package org.example.Rota;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Data.TaxiData;

import java.io.IOException;

public class TaxiController extends Vehicle {
    private double openingFee;
    private double costPerKm;
    private TaxiData taxiData;

    public TaxiController() throws IOException {
        this.taxiData = new TaxiData(new ObjectMapper());
        setCostPerKm();
        setOpeningFee();
    }

    @Override
    public String get_name(){
        return "taxi";
    }

    @Override
    double get_sure(double distance) {
        return distance*5;
    }

    @Override
    double get_ucret(double distance) {
        return openingFee + (costPerKm*distance);
    }

    public void setCostPerKm() {
        this.costPerKm = taxiData.getCostPerKm();
    }

    public void setOpeningFee() {
        this.openingFee = taxiData.getCostPerKm();
    }
}
