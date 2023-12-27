package com.rickiey_innovates.juditonspringapp.models;

public class GetVoucher {
    String particulars, rate, wt, vt, pf;

    public GetVoucher(String particulars, String rate, String wt, String vt, String pf) {
        this.particulars = particulars;
        this.rate = rate;
        this.wt = wt;
        this.vt = vt;
        this.pf = pf;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getWt() {
        return wt;
    }

    public void setWt(String wt) {
        this.wt = wt;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }
}
