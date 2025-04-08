package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Data.*;
import org.example.Data.DurakVerileri.*;
import org.example.DijkstraAlghorithm.DijkstraPathFinder;
import org.example.DijkstraAlghorithm.PathFinder;
import org.example.FrontEnd.FrontEndInfo;
import org.example.FrontEnd.Konum;
import org.example.Graph.*;
import org.example.Mesafe.DistanceCalculator;
import org.example.Passenger.PassengerManager;
import org.example.Passenger.Yolcu;
import org.example.Payment.Odeme;
import org.example.Payment.OdemeKontrol;
import org.example.Payment.PaymentManager;
import org.example.Rota.*;
import org.example.AlternativeRota.Taxi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class WebController {
    @Value("${google.maps.api.key:default-key}")
    private String googleMapsApiKey;

    private final DurakData durak_data;
    private final Rota rota1;
    private final Rota rota2;
    private final Rota rota3;
    private PassengerManager passengerManager;
    private PaymentManager paymentManager;
    private double baslangicEnlem;
    private double baslangicBoylam;
    private double hedefEnlem;
    private double hedefBoylam;
    private String odemeYontemi;
    private String yolcuTuru;
    private double bakiye;
    private DistanceCalculator distanceCalculator;
    private DefaultData data;
    private OdemeKontrol odemeKontrol;
    private final TaxiData taxiData;
    private final Taxi taxi;
    private final ObjectMapper objectMapper;
    private final JsonNodeData jsonNodeData;
    private final GraphDurakData graphDurakData;
    private final Durak durak;
    private HashMap<String,Yolcu>  yolcuHashMap;

    @Autowired
    public WebController(DurakData durak_data, PassengerManager passengerManager,
                         PaymentManager paymentManager, DistanceCalculator distanceCalculator,
                         DefaultData data, OdemeKontrol odemeKontrol, TaxiData taxiData,
                         Taxi taxi, ObjectMapper objectMapper, JsonNodeData jsonNodeData,
                         GraphDurakData graphDurakData,Durak durak
                         ) throws JsonProcessingException {
        this.objectMapper = objectMapper;
        this.jsonNodeData = jsonNodeData;
        this.graphDurakData =  graphDurakData;

        StopData busStopData = new BusStopData(jsonNodeData);
        StopData tramStopData = new TramStopData(jsonNodeData);
        TransferData busTramTransferData = new BusTramTransferData(jsonNodeData,busStopData.getStops(),tramStopData.getStops());


        this.distanceCalculator = distanceCalculator;
        this.durak = durak;

        IGraphBuilder busGraphBuilder = new BusGraphBuilder();
        IGraphBuilder tramGraphBuilder = new TramGraphBuilder();
        ITransferGraphBuilder busTramGraphBuilder = new BusTramGraphBuilder();


        PathFinder pathFinder1 = new DijkstraPathFinder(busGraphBuilder.buildGraph(busStopData));
        PathFinder pathFinder2 = new DijkstraPathFinder(tramGraphBuilder.buildGraph(tramStopData));
        PathFinder pathFinder3 = new DijkstraPathFinder(busTramGraphBuilder.buildGraph(busTramTransferData));

        VehicleManager vehicleManager = new VehicleManager();

        this.durak_data = durak_data;

        this.passengerManager = passengerManager;
        this.paymentManager = paymentManager;

        this.data = data;
        this.odemeKontrol = odemeKontrol;
        this.taxiData = taxiData;
        this.taxi = taxi;

        RotaInfoManager rotaInfoManager = new RotaInfoManager();

        this.yolcuHashMap = new HashMap<>();

        this.rota1 = new Rota(pathFinder1,vehicleManager,taxiData,new Durak(graphDurakData,distanceCalculator,busGraphBuilder.get_name()),rotaInfoManager);
        this.rota2 = new Rota(pathFinder2,vehicleManager,taxiData,new Durak(graphDurakData,distanceCalculator,tramGraphBuilder.get_name()),rotaInfoManager);
        this.rota3 = new Rota(pathFinder3,vehicleManager,taxiData,new Durak(graphDurakData,distanceCalculator,busTramGraphBuilder.get_name()),rotaInfoManager);

    }

    @GetMapping("/")
    public String home(Model model) {
        try {
            model.addAttribute("duraklar", durak_data.getDuraklar().toString());
            model.addAttribute("googleMapsApiKey", googleMapsApiKey);
            model.addAttribute("mesaj", "Spring Boot HTML bağlantısı başarılı!");
        } catch (Exception e) {
            model.addAttribute("mesaj", "Error loading JSON: " + e.getMessage());
        }
        return "index";
    }


    @PostMapping(value = "/")
    @ResponseBody
    public Map handlePostRequest(
            @RequestParam("baslangic_enlem") double baslangicEnlem,
            @RequestParam("baslangic_boylam") double baslangicBoylam,
            @RequestParam("hedef_enlem") double hedefEnlem,
            @RequestParam("hedef_boylam") double hedefBoylam,
            @RequestParam("isim") String isim,
            @RequestParam("odeme_yontemi") String odemeYontemi,
            @RequestParam("yolcu_turu") String yolcuTuru,
            @RequestParam("bakiye") double bakiye
            ) throws IOException {

        System.out.println("isim"+isim);
        System.out.println("odemeYontemi:"+odemeYontemi);
        System.out.println("yolcuTuru:"+yolcuTuru);
        System.out.println("bakiye:"+bakiye);
        passengerManager.setYolcu(yolcuTuru);
        paymentManager.setOdeme_turu(odemeYontemi);
        Yolcu yolcu = passengerManager.get_yolcu();
        Odeme odeme = paymentManager.getOdeme_turu();

        Konum konum = new Konum(baslangicEnlem,baslangicBoylam,hedefEnlem,hedefBoylam);
        FrontEndInfo frontEndInfo = new FrontEndInfo(isim,bakiye,yolcu,odeme);

        yolcu.setName(frontEndInfo.getIsim());

        if(!yolcuHashMap.containsKey(yolcu.getName())){
            yolcuHashMap.put(yolcu.getName(),yolcu);
        }else{
            yolcu = yolcuHashMap.get(yolcu.getName());
        }

        yolcu.setKullanim_sayisi(yolcu.getKullanim_sayisi()+1);

        System.out.println("YolcuHashMap :  ");
        for(Yolcu yolcu_: yolcuHashMap.values()){
            System.out.println(yolcu_.getKullanim_sayisi());
        }
        System.out.println(yolcu.getKullanim_sayisi());

        return rotaCiz(konum,frontEndInfo);
    }

    public Map rotaCiz(Konum konum,FrontEndInfo frontEndInfo) throws IOException {
        Double bakiye = frontEndInfo.getBakiye();
        Yolcu yolcu = frontEndInfo.getYolcu_turu();
        Odeme odeme = frontEndInfo.getOdeme_turu();
        System.out.println("Yolcu nesnesi : " + yolcu);
        System.out.println("Odeme nesnesi : " + odeme);

        RotaInfoManager rotaInfoManager = new RotaInfoManager();

        double taxiDistance = distanceCalculator.calculateDistance(konum.getBaslangicEnlem(), konum.getBaslangicBoylam(), konum.getHedefEnlem(), konum.getHedefBoylam());
        RotaInfo taxiAlternative = taxi.createAlternative(konum, taxiDistance);

        rotaInfoManager.add_path_info(rota1.findRouteWithCoordinates(konum, "ucret"),"bus_ucret");
        rotaInfoManager.add_path_info(rota1.findRouteWithCoordinates(konum, "sure"),"bus-sure");
        rotaInfoManager.add_path_info(rota1.findRouteWithCoordinates(konum, "mesafe"),"bus_mesafe");

        rotaInfoManager.add_path_info(rota2.findRouteWithCoordinates(konum, "ucret"),"tram-ucret");
        rotaInfoManager.add_path_info(rota2.findRouteWithCoordinates(konum, "sure"),"tram-sure");
        rotaInfoManager.add_path_info(rota2.findRouteWithCoordinates(konum, "mesafe"),"tram-mesafe");

        rotaInfoManager.add_path_info(rota3.findRouteWithCoordinates(konum, "ucret"),"bus_tram-ucret");
        rotaInfoManager.add_path_info(rota3.findRouteWithCoordinates(konum, "sure"),"bus_tram-sure");
        rotaInfoManager.add_path_info(rota3.findRouteWithCoordinates(konum, "mesafe"),"bus_tram-mesafe");

        rotaInfoManager.add_path_info(taxiAlternative,"taxi");

        odemeKontrol.kontrol(rotaInfoManager.getAllPath_info(),odeme,yolcu,bakiye);

        System.out.println(rotaInfoManager.getAllPath_info().keySet());
        System.out.println(rotaInfoManager.getAllPath_info());

        return rotaInfoManager.getAllPath_info();
    }


}
