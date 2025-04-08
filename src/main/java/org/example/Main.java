package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Data.*;
import org.example.Data.DurakVerileri.*;
import org.example.Data.Duraklar.Transfer;
import org.example.DijkstraAlghorithm.DijkstraPathFinder;
import org.example.Graph.*;
import org.example.DijkstraAlghorithm.PathFinder;
import org.example.Mesafe.DistanceCalculator;
import org.example.Mesafe.HaversineDistanceCalculator;
import org.example.Passenger.PassengerManager;
import org.example.Payment.OdemeKontrol;
import org.example.Payment.PaymentManager;
import org.example.Rota.*;
import org.example.AlternativeRota.Taxi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    public PassengerManager passengerManager(){
        return new PassengerManager();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public JsonNodeData jsonNodeData(ObjectMapper objectMapper) throws IOException {
        return  new JsonNodeData(objectMapper);
    }

    @Bean
    public GraphDurakData graphDurakData(ObjectMapper objectMapper,JsonNodeData jsonNodeData) throws IOException {
        return new GraphDurakData(objectMapper,jsonNodeData);
    }

    @Bean
    public StopData busstopData(JsonNodeData jsonNodeData) throws JsonProcessingException {
        return new BusStopData(jsonNodeData);
    }

    @Bean
    public StopData tramstopData(JsonNodeData jsonNodeData) throws JsonProcessingException {
        return new TramStopData(jsonNodeData);
    }

    @Bean
    public Durak durak(GraphDurakData graphDurakData,DistanceCalculator distanceCalculator){
        return new Durak(graphDurakData,distanceCalculator,"");
    }

    @Bean
    public IGraphBuilder busgraphbuilder(){
        return new BusGraphBuilder();
    }

    @Bean
    public IGraphBuilder tramgraphbuilder(){
        return new TramGraphBuilder();
    }

    @Bean
    public ITransferGraphBuilder bustramgraphbuilder(){
        return new BusTramGraphBuilder();
    }


    @Bean
    public PaymentManager paymentManager(){
        return new PaymentManager();
    }

    @Bean
    public DistanceCalculator distanceCalculator(){
        return new HaversineDistanceCalculator();
    }

    @Bean
    public DurakData durak_data() throws IOException {
        return new DurakData();
    }

    @Bean
    public OdemeKontrol odemeKontrol(){
        return new OdemeKontrol();
    }
    @Bean
    public DefaultData data() throws IOException {
        return new DefaultData();
    }
    @Bean
    public TaxiData taxiData() throws IOException {
        return new TaxiData(new ObjectMapper());
    }
    @Bean
    public Taxi taxi(TaxiData taxiData) {
        return new Taxi(taxiData);
    }

}

