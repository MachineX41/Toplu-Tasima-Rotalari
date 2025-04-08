package org.example.Data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DijkstraAlghorithm.Coordinate;

import java.io.IOException;
import java.util.HashMap;

public class GraphDurakData extends AbstractData{
    private String veri;
    private ObjectMapper objectMapper;
    private HashMap<Coordinate, JsonNode> duraklar;
    private final JsonNodeData jsonNodeData;

    public GraphDurakData(ObjectMapper objectMapper,JsonNodeData jsonNodeData) throws IOException {
        super();

        this.veri = super.get_data();
        this.duraklar =  new HashMap<>();

        this.objectMapper = objectMapper;

        this.jsonNodeData = jsonNodeData;
        this.set_hashmap_duraklar();
    }

    public void set_hashmap_duraklar() throws JsonProcessingException {

        JsonNode jsonData = jsonNodeData.get_node_data();


        for (JsonNode durak : jsonData.get("duraklar")) {

            duraklar.put(new Coordinate(durak.get("lat").asText(),durak.get("lon").asText()), durak);
        }

    }

    public HashMap<Coordinate,JsonNode> get_hashmap_duraklar(){
        return this.duraklar;
    }



}
