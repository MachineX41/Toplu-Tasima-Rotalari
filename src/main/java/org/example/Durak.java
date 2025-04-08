package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.Data.GraphDurakData;
import org.example.DijkstraAlghorithm.Coordinate;
import org.example.Mesafe.DistanceCalculator;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Durak {
    private final DistanceCalculator distanceCalculator;
    private final GraphDurakData graphDurakData;
    private final String type;

    Durak(GraphDurakData graphDurakData, DistanceCalculator distanceCalculator,String type)  {
        this.graphDurakData = graphDurakData;
        this.distanceCalculator = distanceCalculator;
        this.type = type;
    }

    public Map.Entry<String, Double> findNearestStop(double lat, double lon) {
        double minDistance = Double.POSITIVE_INFINITY;
        String nearestStopId = null;

        for (Map.Entry<Coordinate, JsonNode> entry : graphDurakData.get_hashmap_duraklar().entrySet()) {
            JsonNode stop = entry.getValue();

            if(!type.equals("") && !stop.get("type").asText().equals(type)) {
                continue;
            }
            double distance = distanceCalculator.calculateDistance(lat, lon,
                    stop.get("lat").asDouble(),
                    stop.get("lon").asDouble());
            if (distance < minDistance) {
                minDistance = distance;
                nearestStopId = stop.get("id").asText();
            }
        }

        return new AbstractMap.SimpleEntry<>(nearestStopId, minDistance);
    }
}
