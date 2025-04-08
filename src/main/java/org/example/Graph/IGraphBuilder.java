package org.example.Graph;

import org.example.Data.DurakVerileri.StopData;


public interface IGraphBuilder {
    Graph buildGraph(StopData stopData);
    String get_name();
}
