package org.example.Data.DurakVerileri;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.Data.Duraklar.*;
import org.example.Data.JsonNodeData;
import org.example.DijkstraAlghorithm.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class StopData {
    private HashMap<String, Stop> stops;
    private JsonNodeData jsonNodeData;

    StopData(JsonNodeData jsonNodeData) {
        this.jsonNodeData = jsonNodeData;
        stops = new HashMap<>();
    }

    public abstract void set_stop() throws JsonProcessingException;

    public HashMap<String,Stop> getStops() {
        return stops;
    }

    public JsonNodeData getJsonNodeData() {
        return jsonNodeData;
    }

    protected void addNextStop(HashMap<String,Stop> stops) throws JsonProcessingException {
        System.out.println(stops);
        for(JsonNode durak : jsonNodeData.get_node_data().get("duraklar")){
            Stop stop = stops.get(durak.get("id").asText());
            if(stop != null) {
                for (JsonNode next_stop_json : durak.get("nextStops")) {
                    Stop next_stop = stops.get(next_stop_json.get("stopId").asText());
                    stop.getNextStops().add(next_stop);
                    stop.add_mesafe(next_stop, next_stop_json.get("mesafe").asDouble());
                    stop.add_sure(next_stop, next_stop_json.get("sure").asDouble());
                    stop.add_ucret(next_stop, next_stop_json.get("ucret").asDouble());
                }
            }
        }
    }

    protected void set_stop_info(Stop stop,JsonNode durak){
        stop.setId(durak.get("id").asText());
        stop.setType(durak.get("type").asText());
        stop.setCoordinate(new Coordinate(durak.get("lat").asText(),durak.get("lon").asText()));
    }


}
