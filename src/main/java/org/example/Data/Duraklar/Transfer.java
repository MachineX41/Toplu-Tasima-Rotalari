package org.example.Data.Duraklar;

public abstract class Transfer {
    Stop station1;
    Stop station2;
    double transfer_ucret;
    double transfer_sure;

    public void setStation1(Stop station1) {
        this.station1 = station1;
    }

    public void setStation2(Stop station2) {
        this.station2 = station2;
    }

    public void setTransfer_ucret(double ucret) {
        this.transfer_ucret = ucret;
    }

    public void setTransfer_sure(double sure) {
        this.transfer_sure = sure;
    }

    public double getTransfer_sure() {
        return transfer_sure;
    }

    public double getTransfer_ucret() {
        return transfer_ucret;
    }

    public Stop getStation1() {
        return station1;
    }

    public Stop getStation2() {
        return station2;
    }
}
