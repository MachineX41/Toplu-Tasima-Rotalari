package org.example.Data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TaxiData extends AbstractData{
    private JsonNode taxiData;
    private double openingFee;
    private double costPerKm;
    private final String data;
    private final ObjectMapper objectMapper;

    public TaxiData(ObjectMapper objectMapper) throws IOException {
        super();
        this.data = super.get_data();
        this.objectMapper = objectMapper;
        setTaxiData();
        setCostPerKm();
        setOpeningFee();
    }

    public void setTaxiData() throws JsonProcessingException {
        this.taxiData = this.objectMapper.readTree(this.data).get("taxi");
    }

    public void setOpeningFee(){
        this.openingFee = taxiData.get("openingFee").asDouble();
    }

    public void setCostPerKm(){
        this.costPerKm = taxiData.get("costPerKm").asDouble();
    }

    public double getCostPerKm() {
        return this.costPerKm;
    }

    public double getOpeningFee() {
        return this.openingFee;
    }

}
