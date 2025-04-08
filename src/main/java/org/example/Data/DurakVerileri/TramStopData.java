package org.example.Data.DurakVerileri;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.Data.Duraklar.BusStop;
import org.example.Data.Duraklar.Stop;
import org.example.Data.Duraklar.TramStop;
import org.example.Data.JsonNodeData;

import java.util.HashMap;

public class TramStopData extends StopData{
    private HashMap<String, Stop> stops;
    private JsonNodeData jsonNodeData;

    public TramStopData(JsonNodeData jsonNodeData) throws JsonProcessingException {
        super(jsonNodeData);
        this.jsonNodeData = getJsonNodeData();
        stops = getStops();
        this.set_stop();
    }

    @Override
    public void set_stop() throws JsonProcessingException {
        JsonNode jsonData = jsonNodeData.get_node_data();

        for(JsonNode durak : jsonData.get("duraklar")){
            if(durak.get("type").asText().equals("tram")){
                Stop tram_stop = new TramStop();
                super.set_stop_info(tram_stop,durak);
                stops.put(tram_stop.getId(),tram_stop);
            }
        }

        super.addNextStop(stops);

    }
}
