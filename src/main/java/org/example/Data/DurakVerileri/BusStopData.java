package org.example.Data.DurakVerileri;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.Data.Duraklar.BusStop;
import org.example.Data.Duraklar.Stop;
import org.example.Data.JsonNodeData;

import java.util.HashMap;

public class BusStopData extends StopData{
    private HashMap<String, Stop> stops;
    private JsonNodeData jsonNodeData;

    public BusStopData(JsonNodeData jsonNodeData) throws JsonProcessingException {
        super(jsonNodeData);
        this.jsonNodeData = getJsonNodeData();
        stops = getStops();
        this.set_stop();
    }

    @Override
    public void set_stop() throws JsonProcessingException {
        JsonNode jsonData = jsonNodeData.get_node_data();

        for(JsonNode durak : jsonData.get("duraklar")){
            if(durak.get("type").asText().equals("bus")){
                Stop bus_stop = new BusStop();
                super.set_stop_info(bus_stop,durak);
                stops.put(bus_stop.getId(),bus_stop);
            }
        }

        super.addNextStop(stops);
    }


}
