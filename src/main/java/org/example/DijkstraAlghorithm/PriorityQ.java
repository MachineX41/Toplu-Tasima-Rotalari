package org.example.DijkstraAlghorithm;

import org.example.Graph.Node;

import java.util.ArrayList;
import java.util.HashMap;


public class PriorityQ {
    private HashMap<Double,Node> nodes;

    public PriorityQ(){
        this.nodes = new HashMap<Double,Node>();
    }

    public HashMap<Double,Node> get_queue(){
        return this.nodes;
    }

    public void add_node(Double priority,Node node){
        this.nodes.put(priority,node);
    }

    public int size(){
        return this.nodes.size();
    }

    public Node pop(){
        Double min = Double.MAX_VALUE;
        for(Double priority:this.nodes.keySet()){
            if (priority < min){
                min = priority;
            }
        }
        Node node = this.nodes.get(min);
        this.nodes.remove(min);
        return node;
    }
}