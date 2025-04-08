package org.example.DijkstraAlghorithm;

import org.example.Graph.Edge;
import org.example.Graph.Node;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import org.example.Graph.Graph;

public class DijkstraA {
    private Dictionary[] Dijkstra(Graph graph,Node start_node){
        Dictionary<String, Double> distances = new Hashtable<String, Double>();
        Dictionary<String, Node> previous = new Hashtable<String, Node>();
        PriorityQ pq = new PriorityQ();

        pq.add_node(0.0,start_node);

        for(Node node:graph.getNodes()){
            if(node!=start_node){
                distances.put(node.get_name(),Double.POSITIVE_INFINITY);
            }
            previous.put(node.get_name(),new Node("Null"));
        }

        distances.put(start_node.get_name(),0.0);

        while (pq.size() != 0){
            Node curr_node = pq.pop();

            for(Edge edge : curr_node.get_edges()){

                Double alternative = distances.get(curr_node.get_name()) + edge.getWeight();
                String neighbor = edge.getEnd().get_name();
                if(alternative<distances.get(neighbor)){

                    distances.put(neighbor,alternative);
                    previous.put(neighbor,curr_node);
                    pq.add_node(alternative,edge.getEnd());
                }
            }
        }

        return new Dictionary[]{distances,previous};
    }

    public ArrayList<Node> shortest_path(Graph graph, Node start_node, Node end_node){
        Dictionary[] dictionaries = Dijkstra(graph,start_node);
        Dictionary distances = dictionaries[0];
        Dictionary previous = dictionaries[1];

        Double distance = (Double) distances.get(end_node.get_name());
        System.out.println(start_node.get_name() + " - " + end_node.get_name() + " distance :" + distance);

        ArrayList<Node> path = new ArrayList<Node>();
        Node curr_node = end_node;
        while(previous.get(curr_node.get_name())!=null){
            path.add(0,curr_node);
            curr_node = (Node) previous.get(curr_node.get_name());
        }
        System.out.println("PATH :" + path);
        return path;
    }

}
