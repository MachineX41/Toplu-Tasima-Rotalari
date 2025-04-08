package org.example.DijkstraAlghorithm;

import org.example.Graph.Node;
import org.example.Rota.RotaInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Yol bulucu arayüzü
public interface PathFinder {
    ArrayList<Node> findBestPath(String startId, String stopId, String optimization);
}