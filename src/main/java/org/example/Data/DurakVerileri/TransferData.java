package org.example.Data.DurakVerileri;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Data.Duraklar.Stop;
import org.example.Data.Duraklar.Transfer;
import org.example.Data.JsonNodeData;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TransferData {
    private ArrayList<Transfer> transfers;
    private JsonNodeData jsonNodeData;
    private HashMap<String, Stop> bus_stops;
    private HashMap<String, Stop> tram_stops;

    TransferData(JsonNodeData jsonNodeData, HashMap<String, Stop> bus_stops,HashMap<String,Stop> tram_stops) {
        this.jsonNodeData = jsonNodeData;
        this.bus_stops = bus_stops;
        this.tram_stops = tram_stops;
        transfers = new ArrayList<>();
    }

    public abstract void set_transfer() throws JsonProcessingException;

    public JsonNodeData get_jsonNodeData() {
        return jsonNodeData;
    }

    public ArrayList<Transfer> getTransfers() {
        return transfers;
    }

    public HashMap<String, Stop> getBus_stops() {
        return bus_stops;
    }

    public HashMap<String, Stop> getTram_stops() {
        return tram_stops;
    }

    public HashMap<String,Stop> getMerged_stops() {
        HashMap<String,Stop> merged_stops = new HashMap<>();
        merged_stops.putAll(tram_stops);
        merged_stops.putAll(bus_stops);
        return merged_stops;
    }
}
