package org.example.Graph;

import org.example.Data.DurakVerileri.TransferData;
import org.example.Data.Duraklar.Stop;
import org.example.Data.DurakVerileri.StopData;
import org.example.Data.Duraklar.Transfer;
import org.example.DijkstraAlghorithm.Coordinate;

public class BusTramGraphBuilder implements ITransferGraphBuilder{
    public Graph buildGraph(TransferData transferData) {
        Graph graph = new Graph(false,true);

        for (Stop stop : transferData.getMerged_stops().values()) {
            Coordinate coordinate = stop.getCoordinate();
            graph.addNode(stop.getId(), Double.parseDouble(coordinate.lat()), Double.parseDouble(coordinate.lon()));
        }


        for (Stop stop : transferData.getMerged_stops().values()) {
            String stopId = stop.getId();
            Node stopNode = graph.getNode(stopId);

            if (!stop.getNextStops().isEmpty()) {
                for (Stop nextStop : stop.getNextStops()) {
                    String targetStopId = nextStop.getId();
                    Node targetStopNode = graph.getNode(targetStopId);
                    Double mesafe = stop.getMesafe().get(nextStop);
                    Double sure = stop.getSure().get(nextStop);
                    Double ucret = stop.getUcret().get(nextStop);
                    String type = stop.getType();
                    graph.addEdge(stopNode,targetStopNode, mesafe,sure,ucret,type);
                }
            }
        }

        for(Transfer transfer : transferData.getTransfers()){
            Node transfer_start_node = graph.getNode(transfer.getStation1().getId());
            Node transfer_end_node = graph.getNode(transfer.getStation2().getId());
            Double mesafe = 0.0;
            Double sure = transfer.getTransfer_sure();
            Double ucret = transfer.getTransfer_ucret();;
            graph.addEdge(transfer_start_node,transfer_end_node,mesafe,sure,ucret,"transfer");
        }



        return graph;
    }

    public String get_name(){
        return "";
    }
}
