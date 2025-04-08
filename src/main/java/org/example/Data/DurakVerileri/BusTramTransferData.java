package org.example.Data.DurakVerileri;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.Data.Duraklar.BusTramTransfer;
import org.example.Data.Duraklar.Stop;
import org.example.Data.Duraklar.Transfer;
import org.example.Data.JsonNodeData;

import java.util.ArrayList;
import java.util.HashMap;

public class BusTramTransferData extends TransferData {
    private JsonNodeData jsonNodeData;
    private ArrayList<Transfer> transfers;
    private HashMap<String, Stop> stops;


    public BusTramTransferData(JsonNodeData jsonNodeData,HashMap<String, Stop> bus_stops,HashMap<String,Stop> tram_stops) throws JsonProcessingException {
        super(jsonNodeData,bus_stops,tram_stops);
        this.jsonNodeData = super.get_jsonNodeData();
        this.transfers = getTransfers();
        this.stops = getMerged_stops();
        this.set_transfer();
    }

    @Override
    public void set_transfer() throws JsonProcessingException {
        JsonNode jsonData = super.get_jsonNodeData().get_node_data();

        for(JsonNode durak : jsonData.get("duraklar")){
            if(!durak.get("transfer").isNull()){
                JsonNode transfer_stop = durak.get("transfer");
                Transfer transfer = new BusTramTransfer();
                transfers.add(transfer);
                transfer.setStation1(stops.get(durak.get("id").asText()));
                transfer.setStation2(stops.get(transfer_stop.get("transferStopId").asText()));
                transfer.setTransfer_sure(transfer_stop.get("transferSure").asDouble());
                transfer.setTransfer_ucret(transfer_stop.get("transferUcret").asDouble());
            }
        }

    }

}
