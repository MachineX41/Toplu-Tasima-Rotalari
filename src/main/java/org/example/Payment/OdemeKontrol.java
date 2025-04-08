package org.example.Payment;

import org.example.Passenger.Yolcu;

import java.util.ArrayList;
import java.util.Map;

public class OdemeKontrol {
    public void kontrol(Map<String,Object> rotalar, Odeme odeme, Yolcu yolcu, double bakiye){
        ArrayList index = new ArrayList();
        for (String key:rotalar.keySet()){
            index.add(key);
        }

        for(int i = 0; i<rotalar.size() ;i++){
            Map<String,Object> rota = (Map<String, Object>) rotalar.get(index.get(i));
            double toplam_ucret = (Double) rota.get("toplam_ucret");
            double kalan = odeme.ode(bakiye, toplam_ucret ,yolcu.get_indirim_orani());
            rota.put("toplam_ucret",toplam_ucret - (toplam_ucret* yolcu.get_indirim_orani()));

            if(kalan<0){
                rota.put("bakiye_yeterli",false);
            }
            else {
                rota.put("bakiye_yeterli",true);
                rota.put("kalan_bakiye",kalan);
            }
        }

    }
}
