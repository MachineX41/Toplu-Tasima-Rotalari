package org.example.AlternativeRota;

import org.example.Data.TaxiData;
import org.example.DijkstraAlghorithm.Coordinate;
import org.example.FrontEnd.Konum;
import org.example.Rota.RotaInfo;

import java.util.List;

public class Taxi extends AlternatifRota {
    private final double openingFee;
    private final double costPerKm;

    public Taxi(TaxiData taxiData) {
        this.openingFee = taxiData.getOpeningFee();
        this.costPerKm = taxiData.getCostPerKm();
    }

    public double calculateTaxiFare(double distanceKm) {
        return openingFee + (distanceKm * costPerKm);
    }

    @Override
    public RotaInfo createAlternative(Konum konum,
                                      double distanceKm) {

        RotaInfo rotaInfo = new RotaInfo();
        double taxiFare = calculateTaxiFare(distanceKm);
        double taxiTime = distanceKm * 2; // Taxi time estimate: 2 min/km


        rotaInfo.setToplam_mesafe(distanceKm);
        rotaInfo.setToplam_ucret(taxiFare);
        rotaInfo.setToplam_sure(taxiTime);

        List<Coordinate> taxiWaypoints = createWaypoints(konum.getBaslangicEnlem(), konum.getBaslangicBoylam(), konum.getHedefEnlem(), konum.getHedefBoylam());

        for (Coordinate coordinate:taxiWaypoints){
            rotaInfo.getCoordinates().add(coordinate);
        }

        rotaInfo.addPath_info();

        return rotaInfo;
    }


}