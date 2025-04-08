package org.example.Rota;

import org.example.Data.TaxiData;
import org.example.DijkstraAlghorithm.Coordinate;
import org.example.DijkstraAlghorithm.PathFinder;
import org.example.Durak;
import org.example.FrontEnd.Konum;
import org.example.Graph.Edge;
import org.example.Graph.Node;

import java.io.IOException;
import java.util.*;

public class Rota {
    private final PathFinder pathFinder;
    private final VehicleManager vehicleManager;
    private final TaxiData taxiData;
    private final Durak durak;
    private final RotaInfoManager rotaInfoManager;

    public Rota(PathFinder pathFinder,
                VehicleManager vehicleManager,
                TaxiData taxiData, Durak durak,RotaInfoManager rotaInfoManager) {

        this.durak = durak;
        this.taxiData = taxiData;
        this.pathFinder = pathFinder;
        this.vehicleManager = vehicleManager;
        this.rotaInfoManager = rotaInfoManager;
    }

    public RotaInfo findRouteWithCoordinates(Konum konum, String optimization) throws IOException {
        double startLat = konum.getBaslangicEnlem();
        double startLon = konum.getBaslangicBoylam();
        double endLat = konum.getHedefEnlem();
        double endLon = konum.getHedefBoylam();

        Map.Entry<String, Double> startStop = durak.findNearestStop(startLat, startLon);
        Map.Entry<String, Double> endStop = durak.findNearestStop(endLat,endLon);
        String startId = startStop.getKey();
        double startDistance = startStop.getValue();
        String endId = endStop.getKey();
        double endDistance = endStop.getValue();

        if (startId == null || endId == null) {
            throw new IllegalArgumentException("En yakın durak bulunamadı.");
        }

        RotaInfo rotaInfo = rotaInfoManager.initializePathInfo();

        this.vehicleManager.setVehicle_type(startDistance);
        Vehicle start_vehicle = this.vehicleManager.getVehicle_type();

        start_vehicle.create_way(rotaInfo,new Coordinate(String.valueOf(startLat),String.valueOf(startLon)),startDistance,"start_type");

        ArrayList<Node> shorhtest_path = pathFinder.findBestPath(startId, endId, optimization);

        add_info(rotaInfo,shorhtest_path);

        this.vehicleManager.setVehicle_type(endDistance);
        Vehicle end_vehicle = this.vehicleManager.getVehicle_type();

        end_vehicle.create_way(rotaInfo,new Coordinate(String.valueOf(endLat),String.valueOf(endLon)),endDistance,"end_type");

        return rotaInfo;
    }

    private void add_info(RotaInfo path_info,ArrayList<Node> path) {
        double totalDistance = path_info.getToplam_mesafe();
        double totalTime = path_info.getToplam_sure();
        double totalCost = path_info.getToplam_ucret();

        List<Coordinate> coordinates = path_info.getCoordinates();

        for (int i=0; i < path.size()-1;i++) {
            Node node = path.get(i);
            for(Edge edge : node.get_edges()){
                if(edge.getEnd().equals(path.get(i+1))){
                    totalDistance += edge.getMesafe();
                    totalTime += edge.getSure();
                    totalCost += edge.getUcret();
                }
            }
            coordinates.add(node.getCoordinates());
        }
        coordinates.add(path.get(path.size()-1).getCoordinates());

        path_info.setToplam_mesafe(totalDistance);
        path_info.setToplam_ucret(totalCost);
        path_info.setToplam_sure(totalTime);

        path_info.addPath_info();
    }


}