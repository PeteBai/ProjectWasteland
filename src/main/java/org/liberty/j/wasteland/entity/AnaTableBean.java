package org.liberty.j.wasteland.entity;

public class AnaTableBean {
    String sID;
    String ttID;
    String CID;
    String tDate;
    double tFee;
    String tStatus;
    public AnaTableBean() {
    }

    public AnaTableBean(String sID, String ttID, String CID, String tDate, double tFee, String tStatus) {
        this.sID = sID;
        this.ttID = ttID;
        this.CID = CID;
        this.tDate = tDate;
        this.tFee = tFee;
        this.tStatus = tStatus;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getTtID() {
        return ttID;
    }

    public void setTtID(String ttID) {
        this.ttID = ttID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public double gettFee() {
        return tFee;
    }

    public void settFee(double tFee) {
        this.tFee = tFee;
    }

    public String gettStatus() {
        return tStatus;
    }

    public void settStatus(String tStatus) {
        this.tStatus = tStatus;
    }
}
