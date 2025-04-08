package org.example.Graph;

import java.util.ArrayList;

public class Graph {
    private ArrayList<Node> nodes;
    private final boolean isDirected;
    private final boolean isWeighted;

    public Graph(boolean isDirected, boolean isWeighted) {
        this.nodes = new ArrayList<>();
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
    }

    public Node addNode(String data,double lat,double lon){
        Node new_node = new Node(data);
        new_node.setCoordinates(lat,lon);
        this.nodes.add(new_node);
        return new_node;
    }

    public ArrayList<Node> getNodes() {
        return this.nodes;
    }

    public Node getNode(String data){
        for (Node node : this.nodes) {
            if (node.get_name().equals(data)) {
                return node;
            }
        }
        return null;
    }

    public void addEdge(Node start_node,Node end_node,double mesafe, double sure, double ucret, String baglanti_tipi) {
        if (!isWeighted) {
            mesafe = Double.parseDouble(null);
        }

        start_node.add_edge(end_node, mesafe,sure,ucret,baglanti_tipi);
        if (!isDirected) {
            end_node.add_edge(start_node,mesafe,sure,ucret,baglanti_tipi);
        }
    }

    public boolean isDirected() {
        return isDirected;
    }

    public boolean isWeighted() {
        return isWeighted;
    }

    public Edge getEdge(String start, String end) {
        for (Node node : nodes) {
            for (Edge edge :node.get_edges()){
                if (edge.getStart().get_name().equals(start) && edge.getEnd().get_name().equals(end))
                    return edge;
            }

        }
        return null;
    }

    public ArrayList<Edge> edgeSet(){
        ArrayList<Edge> edges = new ArrayList<>();
        for (Node node : this.getNodes()){
            for (Edge edge : node.get_edges()){
                edges.add(edge);
            }
        }
        return edges;
    }

}
