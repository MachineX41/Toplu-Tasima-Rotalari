package org.example.DijkstraAlghorithm;

import org.example.Graph.Edge;
import org.example.Graph.Node;
import org.example.Graph.Graph;
import org.example.WeightStrategy.*;

import java.util.*;

public class DijkstraPathFinder implements PathFinder {
    private final Graph graph;
    private WeightStrategyManager weight_turu;

    public DijkstraPathFinder(Graph graph) {
        this.graph = graph;
        this.weight_turu = new WeightStrategyManager();
    }

    @Override
    public ArrayList<Node> findBestPath(String startId, String stopId, String optimization) {
        weight_turu.setWeight_turu(optimization);
        WeightStrategy strategy = weight_turu.getWeight_turu();
        System.out.println(strategy);
        for (Edge edge : graph.edgeSet()) {
            edge.setWeight_strategy(strategy.calculateWeight(edge));
        }

        System.out.println("Start ID: " + startId + ", Stop ID: " + stopId);
        DijkstraA dijkstraA = new DijkstraA();
        ArrayList<Node> path = dijkstraA.shortest_path(graph,graph.getNode(startId), graph.getNode(stopId));


        return path;
    }


}