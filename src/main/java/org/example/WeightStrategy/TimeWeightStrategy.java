package org.example.WeightStrategy;

import org.example.Graph.Edge;

public class TimeWeightStrategy implements WeightStrategy {
    @Override
    public double calculateWeight(Edge edge) {
        return edge.getSure();
    }
}
