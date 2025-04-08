package org.example.WeightStrategy;

import org.example.Graph.Edge;

public class DistanceWeightStrategy implements WeightStrategy {
    @Override
    public double calculateWeight(Edge edge) {
        return edge.getMesafe();
    }
}
