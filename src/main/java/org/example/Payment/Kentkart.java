package org.example.Payment;

public class Kentkart implements Odeme{
    @Override
    public double ode(double bakiye, double price,double indirim_orani) {
        return bakiye - (price - (price*indirim_orani));
    }
}
